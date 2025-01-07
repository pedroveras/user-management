import { Component, OnInit } from '@angular/core';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { SharedStandaloneModule } from '../shared-standalone-module';
import { UserService } from '../user.service';
import { ApiResponse } from '../data/apiResponse';
import { Page } from '../data/page';
import { User } from '../data/user';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { CreateUserComponent } from '../create-user/create-user.component';
import Utils from '../service/localstore.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'pr-BR'},
  ],
  imports: [SharedStandaloneModule],
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  page: number = 0;
  totalElements: number = 0;
  totalPages: number = 0;

  constructor(private userService: UserService, private dialog: MatDialog, private router: Router) {}

  ngOnInit(): void {
    this.loadUsers(this.page, 10);
  }

  loadUsers(page: number, size: number): void {
    this.userService.all(page, size).subscribe(
      (response: ApiResponse<Page<User>>) => {
        this.users = response.data.content;
        this.totalElements = response.data.totalElements;
        this.totalPages = response.data.totalPages;
        console.log(this.users);
      },
      (error) => {
        console.error('Error loading users', error);
      }
    );
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadUsers(this.page, 10);
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadUsers(this.page, 10);
    }
  }

  editUser(user: User): void {
    this.openEditUserDialog(user);
  }

  deleteUser(user: User): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.delete(user.id).subscribe(() => {
          this.loadUsers(this.page, 10);
        });
      }
    });
  }

  openCreateUserDialog(): void {
    const dialogRef = this.dialog.open(CreateUserComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsers(this.page, 10);
      }
    });
  }

  openEditUserDialog(user: User): void {
    const dialogRef = this.dialog.open(CreateUserComponent, {
      width: '400px',
      data: { user }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsers(this.page, 10);
      }
    });
  }

  logout() {
    Utils.cleanAuth();
    this.router.navigateByUrl('/login');
  }

  title() {
    return 'List of users';
  }

  displayedColumns: string[] = ['id', 'name', 'email', 'profile', 'actions'];
}
