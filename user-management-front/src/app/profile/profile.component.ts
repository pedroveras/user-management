import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Profile} from "../data/profile";
import {UserService} from "../user.service";
import {ProfileService} from "../profile.service";
import {User} from "../data/user";
import {MAT_DIALOG_DATA, MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import { SharedStandaloneModule } from '../shared-standalone-module';
import Utils from '../service/localstore.service';

@Component({
  standalone: true,
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  imports: [SharedStandaloneModule],
})
export class ProfileComponent implements OnInit {
  form: FormGroup;
  profiles: Profile[];
  displayedColumns: string[] = ['name', 'description', 'usersQuantity', 'addUser'];
  profileSelected: { name: string; description: string; id: string; users: User[] };

  constructor(
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router,
    private profileService: ProfileService,
    public dialog: MatDialog,
  ) {
    this.form = this.formBuilder.group({
      name: new FormControl('', [Validators.required]),
      description: new FormControl('')
    });
    this.profiles = [];
    this.profileSelected = {description: "", name: "", users: [], id: ""};
  }

  ngOnInit(): void {
    if (localStorage.getItem('userProfile') !== 'ADMINISTRATOR') {
      this.snackBar.open('User does not allow to see this page', 'Close', {
        duration: 5000,
        verticalPosition: "top"
      });
      this.logout();
      return;
    }
    this.loadProfile();
  }

  loadProfile() {
    this.profileService.all().subscribe(
      (data: Profile[]) => {
        this.profiles = data;
      }
    );
  }

  logout() {
    Utils.cleanAuth();
    this.router.navigateByUrl('/login');
  }

  onSubmit() {
    if (this.form.valid) {
      this.profileService.create(this.form.value).subscribe(
        (data: Profile) => {
          this.loadProfile();
          this.snackBar.open(`Profile ${data.name} created`, 'Close', {
            duration: 5000,
            verticalPosition: "top",
          });
        });
    } else {
      return;
    }
  }

  openDialog(element: Profile) {
    this.dialog.open(DialogAddUser, {
      data: {profile: element.id},
    }).afterClosed().subscribe(() => this.loadProfile());
  }
}

@Component({
  selector: 'dialog-add-user',
  templateUrl: 'dialog-add-user.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatTableModule, MatSlideToggleModule],
})
export class DialogAddUser implements OnInit {
  profile: Profile = {description: "", id: "", name: "", type: ""};
  users: User[] = [];
  displayedColumns: string[] = ['selected', 'name', 'identification', 'profile'];

  constructor(
    private profileService: ProfileService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public params: {
      profile: string;
    }
  ) {
    // userService.all().subscribe(
    //   (data: User[]) => {
    //     this.users = data;
    //     this.profileService.get(params.profile).subscribe(
    //       (data: Profile) => {
    //         this.profile = data;
    //       }
    //     );
    //   }
    // );
  }

  ngOnInit(): void {
  }

  // hasInProfile(element: User): boolean {
  //   return this.profile.users.some(u => u.id === element.id);
  // }

  changeProfile(element: User) {
    this.profileService.addUser(this.profile.id, element.id).subscribe(
      (data: Profile) => {
        this.profile = data;
        this.users.forEach(user => {
          if (user.id === element.id) {
            user.profile.name = '';
            // const userInProfile = this.profile.users.find(user => user.id === element.id);
            // if (userInProfile) {
            //   user.profile = userInProfile.profile;
            // }
          }
        });
      }
    );
  }

}
