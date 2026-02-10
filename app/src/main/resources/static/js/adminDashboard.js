import {
  getDoctors,
  filterDoctors,
  saveDoctor,
} from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

document.addEventListener("DOMContentLoaded", function () {
  loadDoctorCards();

  const searchInput = document.getElementById("searchInput");
  const timeFilter = document.getElementById("timeFilter");
  const specialtyFilter = document.getElementById("specialtyFilter");

  if (searchInput) {
    searchInput.addEventListener("input", filterDoctorsOnChange);
  }

  if (timeFilter) {
    timeFilter.addEventListener("change", filterDoctorsOnChange);
  }

  if (specialtyFilter) {
    specialtyFilter.addEventListener("change", filterDoctorsOnChange);
  }
});

document.addEventListener("DOMContentLoaded", function () {
  const addDoctorButton = document.getElementById("addDoctorButton");
  if (addDoctorButton) {
    addDoctorButton.addEventListener("click", function () {
      openModal("addDoctor");
    });
  }
});

async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById("content");

    if (contentDiv) {
      contentDiv.innerHTML = "";

      if (doctors && doctors.length > 0) {
        doctors.forEach((doctor) => {
          const doctorCard = createDoctorCard(doctor);
          contentDiv.appendChild(doctorCard);
        });
      } else {
        contentDiv.innerHTML = "<p>No doctors found.</p>";
      }
    }
  } catch (error) {
    console.error("Error loading doctor cards:", error);
  }
}

async function filterDoctorsOnChange() {
  const searchInput = document.getElementById("searchInput");
  const timeFilter = document.getElementById("timeFilter");
  const specialtyFilter = document.getElementById("specialtyFilter");

  if (!searchInput || !timeFilter || !specialtyFilter) {
    return;
  }

  const name = searchInput.value.trim() || null;
  const time = timeFilter.value || null;
  const specialty = specialtyFilter.value || null;

  try {
    const result = await filterDoctors(name, time, specialty);

    if (result.doctors && result.doctors.length > 0) {
      renderDoctorCards(result.doctors);
    } else {
      const contentDiv = document.getElementById("content");
      if (contentDiv) {
        contentDiv.innerHTML =
          "<p>No doctors found with the given filters.</p>";
      }
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("An error occurred while filtering doctors");
  }
}

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");

  if (contentDiv) {
    contentDiv.innerHTML = "";

    if (doctors && doctors.length > 0) {
      doctors.forEach((doctor) => {
        const doctorCard = createDoctorCard(doctor);
        contentDiv.appendChild(doctorCard);
      });
    } else {
      contentDiv.innerHTML = "<p>No doctors found.</p>";
    }
  }
}

async function adminAddDoctor() {
  const nameInput = document.getElementById("doctorName");
  const emailInput = document.getElementById("doctorEmail");
  const phoneInput = document.getElementById("doctorPhone");
  const passwordInput = document.getElementById("doctorPassword");
  const specialtyInput = document.getElementById("specialization");
  const timeCheckboxes = document.querySelectorAll(
    'input[name="availability"]:checked',
  );

  const name = nameInput.value.trim();
  const email = emailInput.value.trim();
  const phone = phoneInput.value.trim();
  const password = passwordInput.value;
  const specialty = specialtyInput.value.trim();

  const availableTimes = Array.from(timeCheckboxes).map(
    (checkbox) => checkbox.value,
  );

  if (
    !name ||
    !email ||
    !phone ||
    !password ||
    !specialty ||
    availableTimes.length === 0
  ) {
    alert(
      "Please fill in all fields and select at least one availability time",
    );
    return;
  }

  const token = localStorage.getItem("token");
  if (!token) {
    alert("Authentication token not found. Please log in again.");
    return;
  }

  const doctor = {
    name: name,
    email: email,
    phone: phone,
    password: password,
    specialty: specialty,
    availableTimes: availableTimes,
  };

  try {
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert("Doctor added successfully!");
      closeModal("addDoctor");
      location.reload();
    } else {
      alert("Error adding doctor: " + result.message);
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("An error occurred while adding the doctor");
  }
}

function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = "block";
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = "none";
  }
}
