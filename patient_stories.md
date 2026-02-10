**Title:**
_An patient, I want to view a list of doctors without logging in, so that I can explore options before registering._
**Acceptance Criteria:**
1. Patients can access the doctor directory via a public page.
2. List displays doctor's name, specialty, and brief bio.
3. Search and filter options are functional for basic criteria (e.g., specialty, location).
**Priority:** Medium
**Story Points:** 3
**Notes:**
- Pagination is optional but encouraged for large datasets.

**Title:**
_An patient, I want to sign up using my email and password, so that I can book appointments._
**Acceptance Criteria:**
1. Sign‑up form accepts email, password, and confirmation.
2. Email address is validated for proper format.
3. Password meets minimum strength requirements (e.g., length, complexity).
4. Upon successful registration, a welcome email is sent and user is redirected to login.
**Priority:** High
**Story Points:** 5
**Notes:**
- Duplicate email registrations must be prevented.

**Title:**
_An patient, I want to log into the portal, so that I can manage my bookings._
**Acceptance Criteria:**
1. Patients can enter credentials on a login page.
2. System authenticates and redirects to the patient dashboard.
3. Failed login attempts provide generic error messaging without disclosing details.
**Priority:** High
**Story Points:** 4
**Notes:**
- Rate limiting or captcha can be added for security.

**Title:**
_An patient, I want to log out of the portal, so that I can secure my account._
**Acceptance Criteria:**
1. Patients can click a logout link/button.
2. Session is terminated and the user is redirected to the public homepage.
3. No residual authentication tokens remain in local storage or cookies.
**Priority:** High
**Story Points:** 3
**Notes:**
- Logout should be accessible from all patient pages.

**Title:**
_An patient, I want to log in and book an hour‑long appointment to consult with a doctor, so that I can receive care._
**Acceptance Criteria:**
1. After login, patients can search for doctors by specialty or name.
2. Availability calendar displays available 60‑minute slots.
3. Patient selects a slot and confirms booking.
4. System records the appointment and sends confirmation to patient and doctor.
**Priority:** High
**Story Points:** 8
**Notes:**
- Time zone handling should be considered for multi‑location doctors.

**Title:**
_An patient, I want to view my upcoming appointments, so that I can prepare accordingly._
**Acceptance Criteria:**
1. Patients can view a list of future appointments on the dashboard.
2. Each appointment displays date, time, doctor name, and location or video link.
3. Appointment status (confirmed, pending, canceled) is clearly shown.
4. Patients can click to cancel or reschedule (if permitted by policy).
**Priority:** Medium
**Story Points:** 5
**Notes:**
- Integrate reminders via email or SMS if available.
