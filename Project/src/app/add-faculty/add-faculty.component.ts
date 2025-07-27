import { Component, OnInit } from '@angular/core';
import { FacultyService } from '../services/faculty.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';


interface Faculty {
    id?: string; // ✅ instead of _id
  facultyId?: string;  // This is our unique identifier for display
  fullName: string;
  email: string;
  department: string;
  joiningDate: string;
  departmentType: string;
}

interface SearchParams {
  facultyId: string;
  fullName: string;
  email: string;
  departmentType: string;
  fromDate: string;
  toDate: string;
}

interface DepartmentStats {
  departmentName: string;
  count: number;
  percentage: number;
}

@Component({
  selector: 'app-add-faculty',
  templateUrl: './add-faculty.component.html',
  styleUrls: ['./add-faculty.component.css']
})
export class AddFacultyComponent implements OnInit {

    editingEmail: string = '';
  faculty: Faculty = {
    
    facultyId: '',
    fullName: '',
    email: '',
    department: 'Computer Science [PG]',
    joiningDate: '',
    departmentType: 'PG'
  };

  facultyList: Faculty[] = [];
  filteredFaculty: Faculty[] = [];
  paginatedFaculty: Faculty[] = [];
  departmentStats: DepartmentStats[] = [];

  searchParams: SearchParams = {
    facultyId: '',
    fullName: '',
    email: '',
    departmentType: '',
    fromDate: '',
    toDate: ''
  };

  loading = false;
  showFacultyList = false;
  manageMode = false;
  showStats = false;
  isEditing = false;
  editingMongoId: string | null = null; // Changed to use MongoDB _id

  currentPage = 1;
  itemsPerPage = 10;
  totalPages = 1;
  sortBy = 'fullName';

  totalFacultyCount = 0;
  totalDepartments = 2;
  newHires = 0;
recentRemovals: number = 0;
constructor(
  private facultyService: FacultyService,
  private http: HttpClient,
  private snackBar: MatSnackBar
) {}

  ngOnInit() {
    this.loadFacultyList();
    this.getRemovedFacultyCount();
  }

 // Add this to your loadFacultyList() method to debug the response structure
loadFacultyList() {
  this.facultyService.getAllFaculty().subscribe({
    next: (response: Faculty[]) => {
      console.log('Raw response from backend:', response);
      
      // Debug: Check what fields each faculty object has
      if (response && response.length > 0) {
        console.log('First faculty object keys:', Object.keys(response[0]));
        console.log('First faculty object:', response[0]);
        
        // Check all possible ID fields using type assertion
        const firstFaculty = response[0] as any;
        console.log('ID fields check:');
        console.log('_id:', firstFaculty._id);
        console.log('id:', firstFaculty.id);
        console.log('objectId:', firstFaculty.objectId);
        console.log('mongoId:', firstFaculty.mongoId);
      }
      
      this.facultyList = response;
      this.filteredFaculty = [...this.facultyList];
      this.sortFaculty();
      this.updatePagination();
      this.calculateDashboardStats();
      this.calculateDepartmentStats();
    },
    error: (error: HttpErrorResponse) => {
      console.error('Error loading faculty:', error);
      this.showError('Failed to load faculty list');
    }
  });
}

onSubmit() {
  if (this.loading) return;
  this.loading = true;

  const facultyData = {
    facultyId: this.faculty.facultyId,
    fullName: this.faculty.fullName,
    email: this.faculty.email,
    department: this.faculty.department,
    joiningDate: this.faculty.joiningDate,
    departmentType: this.faculty.departmentType
  };

  // ✅ Use editingEmail (not editingMongoId) for updates
const serviceCall = this.isEditing && this.editingEmail
  ? this.facultyService.updateFacultyByEmail(this.editingEmail, facultyData)
  : this.facultyService.addFaculty(facultyData);

  serviceCall.subscribe({
    next: (response) => {
      console.log('Faculty operation successful:', response);
      this.loading = false;
      this.showSuccess(this.isEditing ? 'Faculty updated successfully!' : 'Faculty added successfully!');
      this.resetForm();
      this.loadFacultyList(); // Refresh
    },
    error: (error: HttpErrorResponse) => {
      console.error('Faculty operation error:', error);
      this.loading = false;
      this.showError(error.error?.message || error.message || 'Failed to process request');
    }
  });
}


editFaculty(faculty: Faculty) {
  if (!faculty.email) {
    this.showError('Email is required');
    return;
  }
this.isEditing = true;
this.editingEmail = faculty.email;

  this.faculty = {
    facultyId: faculty.facultyId, // will not be changed
    fullName: faculty.fullName,
    email: faculty.email,
    department: faculty.department,
    joiningDate: faculty.joiningDate,
    departmentType: faculty.departmentType
  };

  document.querySelector('.icon-heading')?.scrollIntoView({ behavior: 'smooth' });
}

deleteFaculty(faculty: Faculty) {
  const email = faculty.email;

  if (!email) {
    this.showError('Cannot delete faculty: Email is missing');
    return;
  }

  if (confirm(`Are you sure you want to delete ${faculty.fullName || 'faculty member'}?`)) {
    this.facultyService.deleteFacultyByEmail(email).subscribe({
      next: () => {
        this.showSuccess('Faculty deleted successfully!');
        this.recentRemovals++; // ✅ Increment count on successful deletion
        this.loadFacultyList(); // Refresh faculty list
      },
      error: (error: HttpErrorResponse) => {
        console.error('Delete Error:', error);
        this.showError(error.error?.message || 'Failed to delete faculty');
      }
    });
  }
}


  resetForm() {
    this.faculty = {
      facultyId: '',
      fullName: '',
      email: '',
      department: 'Computer Science [PG]',
      joiningDate: '',
      departmentType: 'PG'
    };
    this.isEditing = false;
     this.editingEmail = '';
    this.editingMongoId = null;
    this.updateDepartmentDisplay();
  }

  updateDepartmentDisplay() {
    this.faculty.department = `Computer Science [${this.faculty.departmentType}]`;
  }

  searchFaculty() {
    this.filteredFaculty = this.facultyList.filter(faculty => {
      const idMatch = !this.searchParams.facultyId ||
        (faculty.facultyId && faculty.facultyId.toLowerCase().includes(this.searchParams.facultyId.toLowerCase()));

      const nameMatch = !this.searchParams.fullName ||
        faculty.fullName.toLowerCase().includes(this.searchParams.fullName.toLowerCase());

      const emailMatch = !this.searchParams.email ||
        faculty.email.toLowerCase().includes(this.searchParams.email.toLowerCase());

      const deptMatch = !this.searchParams.departmentType ||
        faculty.departmentType === this.searchParams.departmentType;

      let dateMatch = true;
      if (this.searchParams.fromDate || this.searchParams.toDate) {
        const facultyDate = new Date(faculty.joiningDate);
        if (this.searchParams.fromDate) {
          dateMatch = dateMatch && facultyDate >= new Date(this.searchParams.fromDate);
        }
        if (this.searchParams.toDate) {
          dateMatch = dateMatch && facultyDate <= new Date(this.searchParams.toDate);
        }
      }

      return idMatch && nameMatch && emailMatch && deptMatch && dateMatch;
    });

    this.currentPage = 1;
    this.updatePagination();
  }

  clearSearch() {
    this.searchParams = {
      facultyId: '',
      fullName: '',
      email: '',
      departmentType: '',
      fromDate: '',
      toDate: ''
    };
    this.filteredFaculty = [...this.facultyList];
    this.currentPage = 1;
    this.updatePagination();
  }

  sortFaculty() {
    this.filteredFaculty.sort((a, b) => {
      switch (this.sortBy) {
        case 'fullName': 
          return a.fullName.localeCompare(b.fullName);
        case 'joiningDate': 
          return new Date(a.joiningDate).getTime() - new Date(b.joiningDate).getTime();
        case 'departmentType': 
          return a.departmentType.localeCompare(b.departmentType);
        case 'facultyId':
          return (a.facultyId || '').localeCompare(b.facultyId || '');
        default: 
          return 0;
      }
    });
    this.updatePagination();
  }

  updatePagination() {
    this.totalPages = Math.ceil(this.filteredFaculty.length / this.itemsPerPage);
    
    // Ensure currentPage is within bounds
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = this.totalPages;
    }
    if (this.currentPage < 1) {
      this.currentPage = 1;
    }
    
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedFaculty = this.filteredFaculty.slice(startIndex, endIndex);
    
    console.log('Pagination update:', {
      total: this.filteredFaculty.length,
      currentPage: this.currentPage,
      totalPages: this.totalPages,
      startIndex,
      endIndex,
      paginatedCount: this.paginatedFaculty.length
    });
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  calculateDashboardStats() {
    this.totalFacultyCount = this.facultyList.length;

    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);

    this.newHires = this.facultyList.filter(faculty => {
      const joiningDate = new Date(faculty.joiningDate);
      return joiningDate >= thirtyDaysAgo;
    }).length;

    console.log('Dashboard stats calculated:', {
      totalFacultyCount: this.totalFacultyCount,
      newHires: this.newHires
    });
  }

 getRemovedFacultyCount() {
  this.facultyService.getRemovedFacultyCount().subscribe({
    next: (response) => {
   this.recentRemovals = response.removedCount;

    },
    error: (err) => {
      this.recentRemovals = 0;
    }
  });
}



  calculateDepartmentStats() {
    const pgCount = this.facultyList.filter(f => f.departmentType === 'PG').length;
    const ugCount = this.facultyList.filter(f => f.departmentType === 'UG').length;
    const total = this.facultyList.length;

    this.departmentStats = [
      {
        departmentName: 'Computer Science [PG]',
        count: pgCount,
        percentage: total > 0 ? Math.round((pgCount / total) * 100) : 0
      },
      {
        departmentName: 'Computer Science [UG]',
        count: ugCount,
        percentage: total > 0 ? Math.round((ugCount / total) * 100) : 0
      }
    ];
  }

  toggleViewFaculty() {
    this.showFacultyList = !this.showFacultyList;
    if (this.showFacultyList) {
      this.loadFacultyList();
    }
  }

  toggleManageMode() {
    this.manageMode = !this.manageMode;
    if (this.manageMode && !this.showFacultyList) {
      this.showFacultyList = true;
      this.loadFacultyList();
    }
  }

  toggleViewStats() {
    this.showStats = !this.showStats;
    if (this.showStats) {
      this.calculateDepartmentStats();
    }
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'Close', { 
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', { 
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
  
}