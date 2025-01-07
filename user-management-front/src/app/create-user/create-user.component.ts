import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from '../data/user';
import { SharedStandaloneModule } from '../shared-standalone-module';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { DateAdapter } from '@angular/material/core';
import { MatDatepickerIntl } from '@angular/material/datepicker';
import { ApiResponse } from '../data/apiResponse';

@Component({
  selector: 'app-create-user',
  imports: [SharedStandaloneModule],
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.css'
})
export class CreateUserComponent {
  form: FormGroup;
  user!: User;
  isNew:boolean = false;

  constructor(
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private _adapter: DateAdapter<any>,
    private _intl: MatDatepickerIntl,
    public dialogRef: MatDialogRef<CreateUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this._adapter.setLocale("pt");
    this._intl.changes.next();
    this.form = this.formBuilder.group({
      username: new FormControl('', [Validators.required]),
      secret: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      birthDate: new FormControl('',),
      phone: new FormControl(''),
      email: new FormControl(''),
      profile: new FormControl('')
    });
    
    if (this.data?.user) {
      this.isNew = false;
      this.userService.get(this.data.user.id).subscribe(
        (response: ApiResponse<User>) => {
          const data = response.data;
          this.user = data;
          this.form = this.formBuilder.group({
            name: new FormControl(this.user.name, [Validators.required]),
            birthDate: new FormControl(this.user.birthDate),
            phone: new FormControl(this.user.phone),
            email: new FormControl(this.user.email),
            profile: new FormControl(this.user.profile.name)
          });
          this.form.get('profile')?.disable();
          this.form.get('name')?.disable();
          this.form.get('birthDate')?.disable();
        });
    } else {
      this.isNew = true;
      this.user = {birthDate: "", phone: "", id: "", email: "", name: "", username: "", profile: {id: "", name: "", description: "", type: ""} };
    }
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if (this.form.valid) {
      if(this.isNew){
        this.userService.create(this.form.value).subscribe(
          (response: ApiResponse<String>) => {
            const data = response.data;
            this.snackBar.open(`User ${data} created`, 'Close', {
              duration: 5000,
              verticalPosition: "top",
            });
            this.dialogRef.close(this.user);
          });
      }else{
        this.userService.update(this.user.id, this.form.value).subscribe(
          (response: ApiResponse<User>) => {
            const data = response.data;
            this.snackBar.open(`User ${data.name} updated`, 'Close', {
              duration: 5000,
              verticalPosition: "top",
            });
            this.dialogRef.close(this.user);
          });
      }
    } else {
      return;
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  title() {
    return this.isNew ? 'Create New User': ('User ' + this.user?.username);
  }

}
