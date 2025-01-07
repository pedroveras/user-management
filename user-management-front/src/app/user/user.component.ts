import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from "../user.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {User} from "../data/user";
import {DateAdapter, MAT_DATE_LOCALE} from "@angular/material/core";
import {MatDatepickerIntl} from "@angular/material/datepicker";
import Utils from '../service/localstore.service';
import { SharedStandaloneModule } from '../shared-standalone-module';
import { ApiResponse } from '../data/apiResponse';

@Component({
  standalone: true,
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'pr-BR'},
  ],
  imports: [SharedStandaloneModule],
})
export class UserComponent implements OnInit {
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
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.isNew = false;
      this.userService.get(userId).subscribe(
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
          this.form.get('profile')?.disable();
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
            this.router.navigateByUrl('/login');
            this.snackBar.open(`User ${data} created`, 'Close', {
              duration: 5000,
              verticalPosition: "top",
            });
          });
      }else{
        this.userService.update(this.user.id, this.form.value).subscribe(
          (response: ApiResponse<User>) => {
            const data = response.data;
            this.snackBar.open(`User ${data} updated`, 'Close', {
              duration: 5000,
              verticalPosition: "top",
            });
          });
      }
    } else {
      return;
    }
  }

  logout() {
    Utils.cleanAuth();
    this.router.navigateByUrl('/login');
  }

  protected readonly localStorage = localStorage;

  title() {
    return this.isNew ? 'SignUp': ('User ' + this.user?.username);
  }
}
