{
    let citiesData = []; // 儲存所有的縣市和區域資料

        document.addEventListener("DOMContentLoaded", () => {
            fetch('../data/TwCities.json') // 確保路徑正確
                .then(response => response.json())
                .then(data => {
                    citiesData = data; // 儲存資料到全域變數
                    const citySelect = document.getElementById("city");
                    data.forEach(city => {
                        const option = document.createElement("option");
                        option.value = city.name;
                        option.textContent = city.name;
                        citySelect.appendChild(option);
                    });
                });
        });

        // 根據選擇的縣市來顯示對應的鄉鎮市區
        function loadDistricts(cityName) {
            const districtSelect = document.getElementById("district");
            districtSelect.innerHTML = '<option value="">請選擇鄉鎮市區</option>'; // 清空舊資料

            // 找到對應的縣市資料
            const selectedCity = citiesData.find(city => city.name === cityName);
            if (selectedCity) {
                selectedCity.districts.forEach(district => {
                    const option = document.createElement("option");
                    // option.value = district.zip; // 使用郵遞區號作為值
                    option.textContent = `${district.name}`;
                    // option.textContent = `${district.name} (${district.zip})`; // 顯示行政區名稱與郵遞區號
                    districtSelect.appendChild(option);
                });
            }
        }
}