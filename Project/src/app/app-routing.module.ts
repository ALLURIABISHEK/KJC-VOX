import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { VerificationPageComponent } from './verification-page/verification-page.component';
import { AuthService } from './core/services/auth.service'; // Just added this import
import { RoleselectionComponent } from './role-selection/role-selection.component';
import { UserDashBoardComponent } from './user-dash-board/user-dash-board.component';
import { AdminDashBoardComponent } from './admin-dash-board/admin-dash-board.component';
import { AddFacultyComponent} from './add-faculty/add-faculty.component';
import { AddDepartmentComponent } from './add-deptartment/add-deptartment.component';
import { ManageDepartmentComponent } from './manage-department/manage-department.component';
import { NoticeComponent} from './notice/notice.component';
import {AssignSubjectsComponent} from './assign-subjects/assign-subjects.component';
import { UsersubjectsComponent } from './usersubjects/usersubjects.component';
import { FeedbackFormComponent} from './givefeedback/givefeedback.component';
import {FacultyDashboardComponent} from './faculty-dashboard/faculty-dashboard.component';
import {FacultySubsComponent } from './faculty-subs/faculty-subs.component';
import { FeedbackSummaryComponent } from './facultyfeedback/facultyfeedback.component';
import { FeedbackHistoryComponent } from './feedback-history/feedback-history.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verification-page', component: VerificationPageComponent },
    { path: 'role-selection', component: RoleselectionComponent },
{ path: 'add-department', component: AddDepartmentComponent },
    { path: 'user-dash-board', component: UserDashBoardComponent },
     { path: 'admin-dash-board', component: AdminDashBoardComponent },

     { path: 'add-faculty', component: AddFacultyComponent },
     { path: 'manage-department', component: ManageDepartmentComponent },
      { path: 'notice', component: NoticeComponent },
       { path: 'assign-subjects', component: AssignSubjectsComponent },
 { path: 'usersubjects', component: UsersubjectsComponent },
  { path: 'givefeedback', component: FeedbackFormComponent },
  { path: 'faculty-dashboard', component: FacultyDashboardComponent },
   { path: 'faculty-subs', component: FacultySubsComponent },
  { path: 'facultyfeedback', component: FeedbackSummaryComponent },
  {path: 'feedback-history', component: FeedbackHistoryComponent},
  { path: '**', redirectTo: '/forgotpassword' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthService] // Just added this line
})
export class AppRoutingModule { }
