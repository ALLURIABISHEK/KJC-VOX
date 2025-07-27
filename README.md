# <div align="center">🎙️ **KJC VOX FEEDBACK SYSTEM** 📝</div>

<div align="center">
  
![Header](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6,12,20&height=200&section=header&text=KJC%20VOX&fontSize=45&fontColor=fff&animation=fadeIn&fontAlignY=35&desc=Anonymous%20Student%20Feedback%20Platform&descAlignY=55&descAlign=50)

</div>

<div align="center">
  
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=28&duration=3000&pause=800&color=4A90E2&background=00000000&center=true&vCenter=true&width=600&lines=🔒+Anonymous+Feedback+System;👥+Multi-Role+Platform;📊+Faculty+Insights+Dashboard)](https://git.io/typing-svg)

</div>

<div align="center">

![Java](https://img.shields.io/badge/Java-Jetty-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

</div>

---

## 🌟 **About This Project**

<img align="right" alt="Feedback System" width="400" src="https://user-images.githubusercontent.com/74038190/229223263-cf2e4b07-2615-4f87-9c38-e37600f8381a.gif">

A comprehensive feedback platform developed under **Kristu Jayanti Software Development Centre (KJSDC)**. The system enables anonymous student feedback collection with secure role-based access for students, faculty, and administrators.

### ✨ **Key Features**
- 🔐 **Role-Based Access** - Separate portals for students, faculty & admins
- 🕵️ **Anonymous Feedback** - Complete privacy protection for students
- 📧 **OTP Verification** - Secure email-based authentication
- 📊 **Interactive Dashboards** - Real-time insights for faculty
- 📱 **Responsive Design** - Works seamlessly on all devices

---

## 👨‍🎓 **What Students Can Do**

<div align="center">

<table>
<tr>
<td width="50%" align="center">

**📝 Submit Feedback**
<br>
• Anonymous course evaluations<br>
• Rate faculty performance<br>
• Provide constructive comments<br>
• Secure identity protection

</td>
<td width="50%" align="center">

**🔒 Secure Registration**
<br>
• Email OTP verification<br>
• Auto-generated credentials<br>
• Privacy-first approach<br>
• Easy login process

</td>
</tr>
</table>

</div>

---

## 👩‍🏫 **What Faculty Can Do**

<div align="center">

<table>
<tr>
<td width="33%" align="center">

**📊 View Feedback**
<br>
• Anonymous student reviews<br>
• Course-wise insights<br>
• Performance analytics<br>
• Improvement suggestions

</td>
<td width="33%" align="center">

**📈 Track Progress**
<br>
• Semester comparisons<br>
• Trend analysis<br>
• Rating improvements<br>
• Student satisfaction

</td>
<td width="33%" align="center">

**🎯 Action Planning**
<br>
• Identify weak areas<br>
• Plan improvements<br>
• Monitor changes<br>
• Enhance teaching

</td>
</tr>
</table>

</div>

---

## ⚙️ **What Admins Can Do**

<div align="center">

<table>
<tr>
<td width="50%" align="center">

**👥 Faculty Management**
<br>
• Add new faculty profiles<br>
• Edit faculty information<br>
• Department assignments<br>
• Remove inactive accounts

</td>
<td width="50%" align="center">

**📊 System Monitoring**
<br>
• Platform usage analytics<br>
• Feedback statistics<br>
• User activity tracking<br>
• System health monitoring

</td>
</tr>
</table>

</div>

---

## 🛠️ **Built With**

<div align="center">

### **Backend**
![Java](https://img.shields.io/badge/Java-Backend_Logic-FF6B35?style=for-the-badge&logo=openjdk&logoColor=white)
![Jetty](https://img.shields.io/badge/Embedded_Jetty-Server-FFD900?style=for-the-badge&logo=eclipse-jetty&logoColor=black)
![MongoDB](https://img.shields.io/badge/MongoDB_Atlas-Database-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### **Frontend**
![Angular](https://img.shields.io/badge/Angular-Framework-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-Language-007ACC?style=for-the-badge&logo=typescript&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-Styling-1572B6?style=for-the-badge&logo=css3&logoColor=white)

</div>

---

## 🚀 **Getting Started**

### **Prerequisites**
```bash
☕ Java JDK 11+
🍃 MongoDB Atlas Account
🅰️ Angular CLI
📧 Email Service Configuration
```

### **Installation**
```bash
# Clone the repository
git clone https://github.com/yourusername/kjc-vox.git
cd kjc-vox

# Backend Setup
cd backend
mvn clean install
java -jar target/kjc-vox-backend.jar

# Frontend Setup
cd ../frontend
npm install
ng serve

# Access the application
http://localhost:4200
```

---

## 🗄️ **Database Structure**

<div align="center">

### **Collections**

</div>

```javascript
// Students Collection
{
  "studentId": "STU-2024-001",
  "email": "student@kjc.edu.in",
  "password": "encrypted_password",
  "isVerified": true,
  "registrationDate": "2024-01-15"
}

// Faculty Collection  
{
  "facultyId": "FAC-CS-001",
  "name": "Dr. John Smith",
  "department": "Computer Science",
  "email": "john.smith@kjc.edu.in",
  "joiningDate": "2020-06-15"
}

// Feedback Collection
{
  "feedbackId": "FB-001",
  "facultyId": "FAC-CS-001",
  "courseCode": "CS-101",
  "rating": 4.2,
  "comments": "Excellent teaching methodology",
  "isAnonymous": true,
  "submissionDate": "2024-03-20"
}
```

---

## 🎯 **How It Works**

<div align="center">

**Student Registration** → **OTP Verification** → **Submit Feedback** → **Anonymous Storage**

**Faculty Login** → **View Dashboard** → **Analyze Feedback** → **Implement Changes**

**Admin Access** → **Manage Faculty** → **Monitor System** → **Generate Reports**

</div>

---

## 🔒 **Security Features**

- 🛡️ Complete anonymity protection
- 🔐 OTP-based email verification
- 👤 Role-based access control (Student/Faculty/Admin)
- 📧 Secure email integration
- 🚫 Identity tracking prevention
- 🔒 Encrypted data storage

---

## 📞 **Contact & Support**

<div align="center">

[![GitHub Issues](https://img.shields.io/badge/Issues-Bug_Reports-red?style=for-the-badge&logo=github)](https://github.com/yourusername/kjc-vox/issues)
[![Email](https://img.shields.io/badge/Email-KJSDC_Team-blue?style=for-the-badge&logo=gmail)](mailto:kjcvox@kjc.edu.in)

</div>

---

<div align="center">

![Footer](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6,12,20&height=100&section=footer&animation=fadeIn)

### 🌟 **Built with ❤️ by KJSDC Team for transparent education** 🌟

</div>
