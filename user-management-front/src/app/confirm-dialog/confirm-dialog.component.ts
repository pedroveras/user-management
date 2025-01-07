import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { SharedStandaloneModule } from '../shared-standalone-module';

@Component({
  selector: 'app-confirm-dialog',
  imports: [SharedStandaloneModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css'
})
export class ConfirmDialogComponent {
  constructor(public dialogRef: MatDialogRef<ConfirmDialogComponent>) {}

  onConfirm(): void {
    this.dialogRef.close(true);  // Close dialog and send 'true' when confirmed
  }

  onCancel(): void {
    this.dialogRef.close(false);  // Close dialog and send 'false' when canceled
  }
}
