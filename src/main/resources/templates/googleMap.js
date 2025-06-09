// window.addEventListener("load", () => {
//   navigator.geolocation.getCurrentPosition(
//       (pos) => {
//         const lat = pos.coords.latitude;
//         const lng = pos.coords.longitude;
//
//         fetch("/api/loc/near", {
//           method: "POST",
//           headers: { "Content-Type": "application/json" },
//           body: JSON.stringify({ lat, lng })
//         });
//       },
//       (err) => {
//         console.error("위치 정보를 받아오지 못했습니다:", err);
//       }
//   );
// });
