import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = `${API_BASE_URL}/api/doctors`;

export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
}

export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${doctorId}?token=${token}`, {
      method: "DELETE",
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "",
    };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return {
      success: false,
      message: "Failed to delete doctor",
    };
  }
}

export async function saveDoctor(doctorData, token) {
  try {
    const response = await fetch(`${DOCTOR_API}?token=${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctorData),
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "",
    };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return {
      success: false,
      message: "Failed to save doctor",
    };
  }
}

export async function filterDoctors(name, time, specialty) {
  try {
    const url = new URL(`${DOCTOR_API}/filter`);
    if (name) url.searchParams.append("name", name);
    if (time) url.searchParams.append("time", time);
    if (specialty) url.searchParams.append("specialty", specialty);

    const response = await fetch(url);

    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error("Error filtering doctors:", response.status);
      return { doctors: [] };
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("An error occurred while filtering doctors");
    return { doctors: [] };
  }
}
