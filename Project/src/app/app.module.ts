import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // For [(ngModel)]
import { HttpClientModule } from '@angular/common/http'; // For HTTP requests
import { MatSnackBarModule } from '@angular/material/snack-bar'; // For snackbars
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // Required by Angular Material


// Routing module
import { AppRoutingModule } from './app-routing.module';

// Components
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';

import { VerificationPageComponent } from './verification-page/verification-page.component';
import { UserDashBoardComponent } from './user-dash-board/user-dash-board.component';
import { RoleselectionComponent } from './role-selection/role-selection.component';
import { AdminDashBoardComponent } from './admin-dash-board/admin-dash-board.component';
import { AddFacultyComponent } from './add-faculty/add-faculty.component';
import { AddDepartmentComponent } from './add-deptartment/add-deptartment.component';
import { ManageDepartmentComponent } from './manage-department/manage-department.component';
import { NoticeComponent } from './notice/notice.component';
import { AssignSubjectsComponent } from './assign-subjects/assign-subjects.component';
import { UsersubjectsComponent } from './usersubjects/usersubjects.component';
import { TestTailwindComponent } from './test-tailwind/test-tailwind.component';
import { FeedbackFormComponent } from './givefeedback/givefeedback.component';
import { FacultyDashboardComponent } from './faculty-dashboard/faculty-dashboard.component';
import { FacultySubsComponent } from './faculty-subs/faculty-subs.component';
import { FeedbackSummaryComponent } from './facultyfeedback/facultyfeedback.component';

@NgModule({
declarations: [
  AppComponent,
  HomeComponent,
  RegisterComponent,
  LoginComponent,
  VerificationPageComponent,
  UserDashBoardComponent,
  RoleselectionComponent,
  AdminDashBoardComponent,
  AddFacultyComponent,
  AddDepartmentComponent,
  ManageDepartmentComponent,
  NoticeComponent,
  AssignSubjectsComponent,
  UsersubjectsComponent,
  TestTailwindComponent,
  FeedbackFormComponent,
  FacultyDashboardComponent,
  FacultySubsComponent,
  FeedbackSummaryComponent  // ✅ ONLY components go here
],

imports: [
  BrowserModule,
  AppRoutingModule,
  FormsModule,              // ✅ Correctly kept here
  HttpClientModule,
  BrowserAnimationsModule,
  MatSnackBarModule
],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
