import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, retry, throwError} from 'rxjs';
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class BasicInterceptor implements HttpInterceptor {

  constructor(private snackBar: MatSnackBar) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let cloned = request.clone({
      headers: request.headers.set('Authorization', `Basic ${localStorage.getItem('token')}`)
    });

    const ignoreUrls = ['login.svg', 'find/username'];
    if (ignoreUrls.some(url => request.url.includes(url))) {
      cloned = request;
    }

    return next.handle(cloned).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';
        if (error.error instanceof ErrorEvent) {
          errorMessage = `Um erro ocorreu: ${error.error.message}`;
        } else if (error.error.userMessage) {
          errorMessage = error.error.userMessage;
        } else {
          errorMessage = `Erro ao acessar o servidor com código: ${error.status},
                    retornou a mensagem: ${error.message}`;
        }
        this.snackBar.open(errorMessage, 'Fechar', {
          duration: 5000,
          verticalPosition: "top"
        });
        return throwError(() => errorMessage);
      })
    );
  }

}
