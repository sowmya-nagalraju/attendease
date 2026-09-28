// ============================================================
// GLOBAL API HELPER
// ============================================================

async function fetchJson(url, options = {}) {

    const response = await fetch(url, options);

    if (!response.ok) {

        let message = `Request failed: ${response.status}`;

        try {
            const errorData = await response.json();

            if (errorData.message) {
                message = errorData.message;
            }
        } catch (error) {
            // Ignore JSON parsing error
        }

        throw new Error(message);
    }

    return response.json();
}


// ============================================================
// FORMATTERS
// ============================================================

function formatNumber(value) {

    const number = Number(value);

    if (Number.isNaN(number)) {
        return "0";
    }

    return number.toLocaleString("en-IN", {
        maximumFractionDigits: 2
    });
}


function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// ============================================================
// PAGE NAVIGATION
// ============================================================

const pageTitles = {

    dashboard: "Dashboard",
    students: "Students",
    subjects: "Subjects",
    sessions: "Class Sessions",
    attendance: "Attendance",
    "my-attendance": "My Attendance",
    reports: "Attendance Reports"

};


function showSection(sectionId) {

    const sections =
        document.querySelectorAll(".page-section");

    sections.forEach(section => {
        section.classList.add("hidden");
    });


    const selectedSection =
        document.getElementById(sectionId);

    if (selectedSection) {
        selectedSection.classList.remove("hidden");
    }


    const links =
        document.querySelectorAll(".nav-link");

    links.forEach(link => {

        link.classList.remove("active");

        const href = link.getAttribute("href");

        if (href === "#" + sectionId) {
            link.classList.add("active");
        }

    });


    const pageTitle =
        document.getElementById("pageTitle");

    if (pageTitle) {
        pageTitle.textContent =
            pageTitles[sectionId] || "Dashboard";
    }

}


function handleHashNavigation() {

    let sectionId =
        window.location.hash.substring(1);

    if (!sectionId) {
        sectionId = "dashboard";
    }


    if (!document.getElementById(sectionId)) {
        sectionId = "dashboard";
    }


    showSection(sectionId);


    switch (sectionId) {

        case "dashboard":
            loadDashboard();
            break;

        case "students":
            loadStudents();
            break;

        case "subjects":
            loadSubjects();
            break;

        case "sessions":
            loadSessions();
            break;

        case "attendance":
            loadAttendance();
            break;

        case "my-attendance":
            break;

        case "reports":
            break;

    }

}


window.addEventListener(
    "hashchange",
    handleHashNavigation
);


// ============================================================
// DASHBOARD
// ============================================================

async function loadDashboard() {

    try {

        const [
            students,
            subjects,
            sessions,
            attendance
        ] = await Promise.all([

            fetchJson("/api/students"),
            fetchJson("/api/subjects"),
            fetchJson("/api/sessions"),
            fetchJson("/api/attendance")

        ]);


        document.getElementById("studentCount").textContent =
            students.length;

        document.getElementById("subjectCount").textContent =
            subjects.length;

        document.getElementById("sessionCount").textContent =
            sessions.length;

        document.getElementById("attendanceCount").textContent =
            attendance.length;

    } catch (error) {

        console.error(
            "Dashboard loading error:",
            error
        );

    }

}


// ============================================================
// STUDENTS
// ============================================================

async function loadStudents() {

    const table =
        document.getElementById("studentsTable");

    try {

        const students =
            await fetchJson("/api/students");


        if (students.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="4" class="empty-state">
                        No students found.
                    </td>
                </tr>
            `;

            return;
        }


        table.innerHTML = "";


        students.forEach(student => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>${escapeHtml(student.id)}</td>
                <td>${escapeHtml(student.rollNumber)}</td>
                <td>${escapeHtml(student.name)}</td>
                <td>${escapeHtml(student.email)}</td>
            `;


            table.appendChild(row);

        });


    } catch (error) {

        console.error(
            "Student loading error:",
            error
        );

        table.innerHTML = `
            <tr>
                <td colspan="4" class="empty-state">
                    ${escapeHtml(error.message)}
                </td>
            </tr>
        `;
    }

}


// ============================================================
// ADD STUDENT
// ============================================================

async function addStudent(event) {

    event.preventDefault();


    const rollNumber =
        document.getElementById(
            "studentRollNumber"
        ).value.trim();


    const name =
        document.getElementById(
            "studentName"
        ).value.trim();


    const email =
        document.getElementById(
            "studentEmail"
        ).value.trim();


    if (!rollNumber || !name) {

        alert(
            "Roll number and student name are required."
        );

        return;
    }


    const studentData = {

        rollNumber: rollNumber,
        name: name,
        email: email

    };


    try {

        await fetchJson(
            "/api/students",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(studentData)
            }
        );


        alert(
            "Student added successfully!"
        );


        document.getElementById(
            "studentForm"
        ).reset();


        await loadStudents();


    } catch (error) {

        console.error(
            "Add student error:",
            error
        );

        alert(
            "Unable to add student: " +
            error.message
        );
    }

}


// ============================================================
// SUBJECTS
// ============================================================

async function loadSubjects() {

    const table =
        document.getElementById("subjectsTable");

    try {

        const subjects =
            await fetchJson("/api/subjects");


        if (subjects.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="4" class="empty-state">
                        No subjects found.
                    </td>
                </tr>
            `;

            return;
        }


        table.innerHTML = "";


        subjects.forEach(subject => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>${escapeHtml(subject.id)}</td>
                <td>${escapeHtml(subject.subjectCode)}</td>
                <td>${escapeHtml(subject.subjectName)}</td>
                <td>
                    ${formatNumber(
                subject.minimumAttendancePercentage
            )}%
                </td>
            `;


            table.appendChild(row);

        });


    } catch (error) {

        console.error(
            "Subject loading error:",
            error
        );

        table.innerHTML = `
            <tr>
                <td colspan="4" class="empty-state">
                    ${escapeHtml(error.message)}
                </td>
            </tr>
        `;
    }

}


// ============================================================
// ADD SUBJECT
// ============================================================

async function addSubject(event) {

    event.preventDefault();


    const subjectCode =
        document.getElementById(
            "subjectCode"
        ).value.trim();


    const subjectName =
        document.getElementById(
            "subjectName"
        ).value.trim();


    const minimumAttendance =
        document.getElementById(
            "minimumAttendance"
        ).value;


    if (!subjectCode || !subjectName) {

        alert(
            "Subject code and subject name are required."
        );

        return;
    }


    const subjectData = {

        subjectCode: subjectCode,
        subjectName: subjectName,
        minimumAttendancePercentage:
            Number(minimumAttendance)

    };


    try {

        await fetchJson(
            "/api/subjects",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(subjectData)
            }
        );


        alert(
            "Subject added successfully!"
        );


        document.getElementById(
            "subjectForm"
        ).reset();


        document.getElementById(
            "minimumAttendance"
        ).value = "75";


        await loadSubjects();


    } catch (error) {

        console.error(
            "Add subject error:",
            error
        );

        alert(
            "Unable to add subject: " +
            error.message
        );
    }

}


// ============================================================
// SESSIONS
// ============================================================

async function loadSessions() {

    const table =
        document.getElementById("sessionsTable");


    try {

        const sessions =
            await fetchJson("/api/sessions");


        if (sessions.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="4" class="empty-state">
                        No sessions found.
                    </td>
                </tr>
            `;

            return;
        }


        table.innerHTML = "";


        sessions.forEach(session => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>${escapeHtml(session.id)}</td>
                <td>
                    ${escapeHtml(
                session.subject?.subjectCode
            )}
                </td>
                <td>${escapeHtml(session.sessionDate)}</td>
                <td>${escapeHtml(session.topic)}</td>
            `;


            table.appendChild(row);

        });


    } catch (error) {

        console.error(
            "Session loading error:",
            error
        );

        table.innerHTML = `
            <tr>
                <td colspan="4" class="empty-state">
                    ${escapeHtml(error.message)}
                </td>
            </tr>
        `;
    }

}


// ============================================================
// CREATE SESSION
// ============================================================

async function createSession(event) {

    event.preventDefault();

    const subjectCode =
        document.getElementById(
            "sessionSubjectCode"
        ).value.trim();

    const sessionDate =
        document.getElementById(
            "sessionDate"
        ).value;

    const topic =
        document.getElementById(
            "sessionTopic"
        ).value.trim();

    if (!subjectCode || !sessionDate) {

        alert(
            "Subject code and session date are required."
        );

        return;
    }

    const params = new URLSearchParams();

    params.append(
        "subjectCode",
        subjectCode
    );

    params.append(
        "sessionDate",
        sessionDate
    );

    if (topic) {

        params.append(
            "topic",
            topic
        );
    }

    try {

        await fetchJson(
            "/api/sessions?" + params.toString(),
            {
                method: "POST"
            }
        );

        alert(
            "Session created successfully!"
        );

        document.getElementById(
            "sessionForm"
        ).reset();

        await loadSessions();

    } catch (error) {

        console.error(
            "Create session error:",
            error
        );

        alert(
            "Unable to create session: " +
            error.message
        );
    }
}

// ============================================================
// ATTENDANCE
// ============================================================

async function loadAttendance() {

    const table =
        document.getElementById("attendanceTable");


    try {

        const records =
            await fetchJson("/api/attendance");


        if (records.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="6" class="empty-state">
                        No attendance records found.
                    </td>
                </tr>
            `;

            return;
        }


        table.innerHTML = "";


        records.forEach(record => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>${escapeHtml(record.id)}</td>

                <td>
                    ${escapeHtml(
                record.student?.name
            )}
                </td>

                <td>
                    ${escapeHtml(
                record.session?.id
            )}
                </td>

                <td>
                    ${escapeHtml(
                record.session?.sessionDate
            )}
                </td>

                <td>
                    ${escapeHtml(
                record.session?.subject?.subjectCode
            )}
                </td>

                <td>
                    ${
                record.present
                    ? "Present"
                    : "Absent"
            }
                </td>
            `;


            table.appendChild(row);

        });


    } catch (error) {

        console.error(
            "Attendance loading error:",
            error
        );

        table.innerHTML = `
            <tr>
                <td colspan="6" class="empty-state">
                    ${escapeHtml(error.message)}
                </td>
            </tr>
        `;
    }

}


// ============================================================
// MARK ATTENDANCE
// ============================================================

async function markAttendance(event) {

    event.preventDefault();

    const rollNumber =
        document.getElementById(
            "attendanceRollNumber"
        ).value.trim();

    const sessionId =
        document.getElementById(
            "attendanceSessionId"
        ).value;

    const present =
        document.getElementById(
            "attendanceStatus"
        ).value === "true";

    if (!rollNumber || !sessionId) {

        alert(
            "Roll number and Session ID are required."
        );

        return;
    }

    const attendanceData = {

        rollNumber: rollNumber,
        sessionId: Number(sessionId),
        present: present

    };

    try {

        await fetchJson(
            "/api/attendance",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(attendanceData)
            }
        );

        alert(
            "Attendance marked successfully!"
        );

        document.getElementById(
            "attendanceForm"
        ).reset();

        document.getElementById(
            "attendanceStatus"
        ).value = "true";

        await loadAttendance();

    } catch (error) {

        console.error(
            "Mark attendance error:",
            error
        );

        alert(
            "Unable to mark attendance: " +
            error.message
        );
    }
}

// ============================================================
// CORRECT ATTENDANCE
// ============================================================

async function correctAttendance(event) {

    event.preventDefault();


    const attendanceId =
        document.getElementById(
            "correctionAttendanceId"
        ).value;


    const present =
        document.getElementById(
            "correctionStatus"
        ).value === "true";


    if (!attendanceId) {

        alert(
            "Attendance record ID is required."
        );

        return;
    }


    try {

        await fetchJson(
            `/api/attendance/${attendanceId}?present=${present}`,
            {
                method: "PUT"
            }
        );


        alert(
            "Attendance corrected successfully!"
        );


        document.getElementById(
            "correctionForm"
        ).reset();


        document.getElementById(
            "correctionStatus"
        ).value = "true";


        await loadAttendance();


    } catch (error) {

        console.error(
            "Attendance correction error:",
            error
        );

        alert(
            "Unable to correct attendance: " +
            error.message
        );
    }

}


// ============================================================
// STUDENT ATTENDANCE REPORT
// ============================================================

async function checkAttendance(event) {

    event.preventDefault();


    const studentId =
        document.getElementById(
            "reportStudentId"
        ).value;


    const subjectId =
        document.getElementById(
            "reportSubjectId"
        ).value;


    if (!studentId || !subjectId) {

        alert(
            "Student ID and Subject ID are required."
        );

        return;
    }


    try {

        const percentage =
            await fetchJson(
                `/api/reports/attendance?studentId=${studentId}&subjectId=${subjectId}`
            );


        const shortage =
            await fetchJson(
                `/api/reports/shortage/check?studentId=${studentId}&subjectId=${subjectId}`
            );


        document.getElementById(
            "attendancePercentage"
        ).textContent =
            formatNumber(percentage) + "%";


        document.getElementById(
            "attendanceShortage"
        ).textContent =
            shortage
                ? "SHORTAGE"
                : "ELIGIBLE";


    } catch (error) {

        console.error(
            "Attendance report error:",
            error
        );

        alert(
            "Unable to calculate attendance: " +
            error.message
        );
    }

}


// ============================================================
// SUBJECT ATTENDANCE REPORT
// ============================================================

async function generateSubjectReport(event) {

    event.preventDefault();


    const subjectId =
        document.getElementById(
            "subjectReportId"
        ).value;


    const table =
        document.getElementById(
            "reportTable"
        );


    if (!subjectId) {

        alert(
            "Subject ID is required."
        );

        return;
    }


    try {

        const report =
            await fetchJson(
                `/api/reports/subject/${subjectId}`
            );


        if (!report || report.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="7" class="empty-state">
                        No report data found.
                    </td>
                </tr>
            `;

            return;
        }


        table.innerHTML = "";


        report.forEach(row => {

            const tableRow =
                document.createElement("tr");


            tableRow.innerHTML = `
                <td>
                    ${escapeHtml(row.rollNumber)}
                </td>

                <td>
                    ${escapeHtml(row.studentName)}
                </td>

                <td>
                    ${escapeHtml(row.subjectCode)}
                </td>

                <td>
                    -
                </td>

                <td>
                    -
                </td>

                <td>
                    ${formatNumber(
                row.attendancePercentage
            )}%
                </td>

                <td>
                    ${
                row.shortage
                    ? "YES"
                    : "NO"
            }
                </td>
            `;


            table.appendChild(tableRow);

        });


    } catch (error) {

        console.error(
            "Subject report error:",
            error
        );

        table.innerHTML = `
            <tr>
                <td colspan="7" class="empty-state">
                    ${escapeHtml(error.message)}
                </td>
            </tr>
        `;
    }

}
// ============================================================
// MY ATTENDANCE
// ============================================================

async function loadMyAttendance(event) {

    event.preventDefault();

    // Get the roll number entered by the student
    const rollNumber =
        document.getElementById(
            "myAttendanceRollNumber"
        ).value.trim();

    // Get the subject code entered by the student
    const subjectCode =
        document.getElementById(
            "myAttendanceSubjectCode"
        ).value.trim();


    // Check whether both fields were entered
    if (!rollNumber || !subjectCode) {

        alert(
            "Roll number and subject code are required."
        );

        return;
    }


    // Get the attendance table
    const table =
        document.getElementById(
            "myAttendanceTable"
        );


    try {

        // ====================================================
        // 1. FIND STUDENT USING ROLL NUMBER
        // ====================================================

        const student =
            await fetchJson(
                "/api/students/roll/" +
                encodeURIComponent(rollNumber)
            );


        // ====================================================
        // 2. FIND SUBJECT USING SUBJECT CODE
        // ====================================================

        const subject =
            await fetchJson(
                "/api/subjects/code/" +
                encodeURIComponent(subjectCode)
            );


        // ====================================================
        // 3. GET ALL ATTENDANCE RECORDS FOR THIS STUDENT
        // ====================================================

        const attendanceRecords =
            await fetchJson(
                "/api/attendance/student/" +
                student.id
            );


        // ====================================================
        // 4. CALCULATE ATTENDANCE PERCENTAGE
        // ====================================================

        const percentage =
            await fetchJson(
                `/api/reports/attendance?studentId=${student.id}&subjectId=${subject.id}`
            );


        // ====================================================
        // 5. CHECK WHETHER STUDENT HAS SHORTAGE
        // ====================================================

        const shortage =
            await fetchJson(
                `/api/reports/shortage/check?studentId=${student.id}&subjectId=${subject.id}`
            );


        // ====================================================
        // 6. DISPLAY ATTENDANCE PERCENTAGE
        // ====================================================

        document.getElementById(
            "myAttendancePercentage"
        ).textContent =
            formatNumber(percentage) + "%";


        // ====================================================
        // 7. DISPLAY ATTENDANCE STATUS
        // ====================================================

        document.getElementById(
            "myAttendanceStatus"
        ).textContent =
            shortage
                ? "SHORTAGE"
                : "ELIGIBLE";


        // ====================================================
        // 8. SHOW ONLY THE SELECTED SUBJECT'S RECORDS
        // ====================================================

        const subjectRecords =
            attendanceRecords.filter(record => {

                return record.session &&
                    record.session.subject &&
                    record.session.subject.subjectCode ===
                    subjectCode;

            });


        // ====================================================
        // 9. IF THERE ARE NO RECORDS
        // ====================================================

        if (subjectRecords.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="5" class="empty-state">
                        No attendance records found for
                        ${escapeHtml(subjectCode)}.
                    </td>
                </tr>
            `;

            return;
        }


        // ====================================================
        // 10. CLEAR OLD TABLE DATA
        // ====================================================

        table.innerHTML = "";


        // ====================================================
        // 11. DISPLAY ATTENDANCE RECORDS
        // ====================================================

        subjectRecords.forEach(record => {

            const row =
                document.createElement("tr");


            row.innerHTML = `
                <td>
                    ${escapeHtml(record.session.id)}
                </td>

                <td>
                    ${escapeHtml(
                record.session.sessionDate
            )}
                </td>

                <td>
                    ${escapeHtml(
                record.session.subject.subjectCode
            )}
                </td>

                <td>
                    ${escapeHtml(
                record.session.topic
            )}
                </td>

                <td>
                    ${
                record.present
                    ? "Present"
                    : "Absent"
            }
                </td>
            `;


            table.appendChild(row);

        });


    } catch (error) {

        // ====================================================
        // ERROR HANDLING
        // ====================================================

        console.error(
            "My attendance error:",
            error
        );


        table.innerHTML = `
            <tr>
                <td colspan="5" class="empty-state">
                    ${escapeHtml(error.message)}
                </td>
            </tr>
        `;


        document.getElementById(
            "myAttendancePercentage"
        ).textContent = "-";


        document.getElementById(
            "myAttendanceStatus"
        ).textContent = "-";
    }

}

// ============================================================
// FORM EVENT HANDLERS
// ============================================================

document.addEventListener(
    "DOMContentLoaded",
    () => {

        const studentForm =
            document.getElementById(
                "studentForm"
            );

        if (studentForm) {

            studentForm.addEventListener(
                "submit",
                addStudent
            );
        }
        const myAttendanceForm =
            document.getElementById(
                "myAttendanceForm"
            );

        if (myAttendanceForm) {

            myAttendanceForm.addEventListener(
                "submit",
                loadMyAttendance
            );
        }


        const subjectForm =
            document.getElementById(
                "subjectForm"
            );

        if (subjectForm) {

            subjectForm.addEventListener(
                "submit",
                addSubject
            );
        }


        const sessionForm =
            document.getElementById(
                "sessionForm"
            );

        if (sessionForm) {

            sessionForm.addEventListener(
                "submit",
                createSession
            );
        }


        const attendanceForm =
            document.getElementById(
                "attendanceForm"
            );

        if (attendanceForm) {

            attendanceForm.addEventListener(
                "submit",
                markAttendance
            );
        }


        const correctionForm =
            document.getElementById(
                "correctionForm"
            );

        if (correctionForm) {

            correctionForm.addEventListener(
                "submit",
                correctAttendance
            );
        }


        const reportForm =
            document.getElementById(
                "reportForm"
            );

        if (reportForm) {

            reportForm.addEventListener(
                "submit",
                checkAttendance
            );
        }


        const subjectReportForm =
            document.getElementById(
                "subjectReportForm"
            );

        if (subjectReportForm) {

            subjectReportForm.addEventListener(
                "submit",
                generateSubjectReport
            );
        }


        // Open the correct page.
        handleHashNavigation();

    }
);