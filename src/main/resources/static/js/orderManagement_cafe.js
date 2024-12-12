document.addEventListener("DOMContentLoaded", function() {
	flatpickr("#dateRange", {
		mode: "range",
		dateFormat: "Y-m-d",
		locale: "zh-tw", // 繁體中文
		onClose: function(selectedDates) {
			console.log("選擇的日期範圍：", selectedDates);
		}
	});
});
document.querySelector("#dateRange").addEventListener("change", function() {
	const dateRange = this.value.split(" to ");
	if (dateRange.length === 1) {
		this.value = `${dateRange[0]} to ${dateRange[0]}`; // 自動補全為單一天範圍
	}
});


function shipOrder(button) {
	// 從按鈕的自定義屬性中獲取 orderId
	const orderId = button.getAttribute("data-order-id");
	if (!confirm("確定要出貨嗎？")) {
		return;
	}

	// 發送 Fetch 請求
	fetch(`/cafe_order/ship/${orderId}`, {
		method: "GET",
	})
		.then(response => {
			if (response.ok) {
				button.disabled = true;
				button.classList.add("disabled-btn");
				button.innerHTML = "已出貨";
			} else {
				throw new Error("出貨失敗，請稍後再試");
			}
		})
		.catch(error => {
			alert(error.message);
		});
}
function showDetails(button) {
	const orderId = button.getAttribute("data-order-id");

	fetch(`/cafe_order/details/${orderId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error("無法加載訂單詳情，請檢查後端 API。");
			}
			return response.json();
		})
		.then(data => {
			console.log("返回的訂單詳情數據:", data);

			// 確認包含 details, totalAmount, memo
			const { details, totalAmount, memo } = data;

			if (!Array.isArray(details) || details.length === 0) {
				throw new Error("訂單詳情為空或數據格式錯誤。");
			}

			const modalContainer = document.getElementById("modal-container");
			if (!modalContainer) {
				throw new Error("未找到 modal-container 容器，請檢查 HTML 結構。");
			}

			let modalContent = `
		        <div class="modal-overlay" onclick="closeModal()"></div>
		        <div class="modal">
		            <h2>訂單詳情</h2>
		            <table>
		                <thead>
		                    <tr>
		                        <th>商品名稱</th>
		                        <th>數量</th>
		                        <th>金額</th>
		                    </tr>
		                </thead>
		                <tbody>
		    `;

			details.forEach(item => {
				modalContent += `
		            <tr>
		                <td>${item.itemName}</td>
		                <td>${item.quantity}</td>
		                <td>${item.amount}</td>
		            </tr>
		        `;
			});

			modalContent += `
		                </tbody>
		            </table>
		            <p><strong>總計（含運費）:</strong> ${totalAmount || '未知'}</p>
		            <p><strong>備註:</strong> ${memo || '無'}</p>
		            <button onclick="closeModal()">關閉</button>
		        </div>
		    `;

			modalContainer.innerHTML = modalContent;
			modalContainer.style.display = "block";
		})

		.catch(error => {
			alert(error.message);
			console.error("錯誤信息:", error);
		});
}

function closeModal() {
	const modalContainer = document.getElementById("modal-container");
	if (modalContainer) {
		modalContainer.style.display = "none";
		modalContainer.innerHTML = "";
	}
}

