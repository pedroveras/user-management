import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ImageLoaderService} from '../image-loader.service';
import {UserService} from "../user.service";
import {User} from "../data/user";
import {Router} from "@angular/router";
import { SharedStandaloneModule } from '../shared-standalone-module';
import Utils from '../service/localstore.service';
import { ApiResponse } from '../data/apiResponse';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [SharedStandaloneModule],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  logoUrl: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private imageLoader: ImageLoaderService,
    private userService: UserService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      secret: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadLogo();
  }

  loadLogo() {
    const logoUrl = 'login.svg';
    this.imageLoader.loadImage(logoUrl).subscribe((blob: Blob) => {
      this.logoUrl = URL.createObjectURL(blob);
    });
  }

  onSubmit(): void {
    Utils.cleanAuth();
    if (this.loginForm.valid) {
      this.userService.login(this.loginForm.value).subscribe(
        (response: ApiResponse<User>) => {
          const data = response.data;
          localStorage.setItem('userId', data.id);
          localStorage.setItem('username', data.username);
          localStorage.setItem('userProfile', data.profile.name);
          localStorage.setItem('token', btoa(`${data.username}:${this.loginForm.get('secret')?.value}`));
          console.log('drtgert' + data.profile.name);
          if(data.profile.name === 'ADMIN') {
            this.router.navigateByUrl('/list');
          } else {
            this.router.navigateByUrl('/user');
          }
        });
    } else {
      return;
    }
  }

  signup(){
    Utils.cleanAuth();
    this.router.navigateByUrl('/signup');
  }

}


