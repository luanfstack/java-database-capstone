# Doctor Story Template
**Title:**
_As a [user role], I want [feature/goal], so that [reason]._
**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]
**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

**Title:**
_An doctor, I want to log into the portal, so that I can manage my appointments._
**Acceptance Criteria:**
1. Doctor can enter credentials on a secure login page.
2. System authenticates the credentials and redirects to the doctor’s dashboard.
3. Failed login attempts provide a generic error message without revealing sensitive details.
**Priority:** High
**Story Points:** 4
**Notes:**
- Rate limiting or CAPTCHA can be added for additional security.

**Title:**
_An doctor, I want to log out of the portal, so that I can protect my data._
**Acceptance Criteria:**
1. Doctor can click a logout button from any page.
2. Session is terminated and the doctor is redirected to the public homepage.
3. No authentication tokens remain in local storage or cookies after logout.
**Priority:** High
**Story Points:** 3
**Notes:**
- Logout link should be visible in the header/footer on all doctor pages.

**Title:**
_An doctor, I want to view my appointment calendar, so that I can stay organized._
**Acceptance Criteria:**
1. Doctor can access a calendar view from the dashboard.
2. Calendar displays all confirmed appointments with patient name, time, and status.
3. Doctor can navigate between weeks/months and filter by status (confirmed, pending, canceled).
**Priority:** Medium
**Story Points:** 5
**Notes:**
- Calendar should refresh in real‑time when new appointments are booked or canceled.

**Title:**
_An doctor, I want to mark my unavailability, so that patients only see available slots._
**Acceptance Criteria:**
1. Doctor can select a date/time range and set a “busy” status.
2. Unavailable slots are hidden from the patient booking interface.
3. Doctor can modify or remove unavailability blocks as needed.
**Priority:** Medium
**Story Points:** 4
**Notes:**
- Unavailability should persist across sessions and be reflected in the public schedule view.

**Title:**
_An doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date details._
**Acceptance Criteria:**
1. Doctor can edit fields such as name, specialty, office address, phone number, and email.
2. Validation ensures required fields are not left blank and contact info is in correct format.
3. Updated information is immediately visible to patients browsing the doctor directory.
**Priority:** Medium
**Story Points:** 4
**Notes:**
- Changes should be logged for audit purposes.

**Title:**
_An doctor, I want to view patient details for upcoming appointments, so that I can be prepared._
**Acceptance Criteria:**
1. Doctor can open a detailed view of each upcoming appointment.
2. View includes patient’s name, contact information, medical history summary, and reason for visit.
3. Doctor can add notes or updates to the appointment record.
**Priority:** Medium
**Story Points:** 5
**Notes:**
- Patient data must be protected in compliance with privacy regulations.