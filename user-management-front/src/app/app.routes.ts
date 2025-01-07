import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { UserComponent } from './user/user.component';
import { UserListComponent } from './user-list/user-list.component';

export const routes: Routes = [
      {
        path: '',
        component: LoginComponent,
      },
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'profile',
        component: ProfileComponent,
      },
      {
        path: 'user',
        component: UserComponent,
      },
      {
        path: 'signup',
        component: UserComponent,
      },
      {
        path: 'list',
        component: UserListComponent,
      },
];
