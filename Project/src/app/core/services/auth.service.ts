import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser: any = null;

  constructor() {
    this.loadUserFromSession();
  }

  // Set current user and store only email in sessionStorage
setCurrentUser(userData: any) {
  this.currentUser = userData;
  if (userData?.email) {
    sessionStorage.setItem('userData', JSON.stringify(userData));
  }
}


getCurrentUser() {
  if (this.currentUser) return this.currentUser;

  const stored = sessionStorage.getItem('userData');
  if (stored) {
    this.currentUser = JSON.parse(stored);
    return this.currentUser;
  }
  return null;
}


  // Get email from memory or sessionStorage (safe for reloads)
  getEmail(): string | null {
    if (this.currentUser?.email) {
      return this.currentUser.email;
    }
const stored = sessionStorage.getItem('userData');
if (stored) {
  const user = JSON.parse(stored);
  return user.email;
}
return null;
  }

  // Check verification status
  isVerified(): boolean {
    return this.currentUser?.isVerified || false;
  }

  // Clear both memory and session
clearUser() {
  this.currentUser = null;
  sessionStorage.removeItem('userData'); // ✅ remove full userData
}


  // Restore email on app start or reload
private loadUserFromSession() {
  const stored = sessionStorage.getItem('userData');
  if (stored) {
    this.currentUser = JSON.parse(stored);
  }
}


getRole(): string | null {
  return this.getCurrentUser()?.role || null;
}


}
