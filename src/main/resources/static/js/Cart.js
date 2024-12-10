// 確保 Footer 不會遮擋底部商品
window.addEventListener('load', () => {
	const footerHeight = document.querySelector('.footer').offsetHeight;
	document.querySelector('.cart-container').style.marginBottom = `${footerHeight}px`;
});

// 更新總價格與商品數量
function updateTotal() {
	try {
		let total = 0;
		let count = 0;

		const itemCheckboxes = document.querySelectorAll('.item-checkbox');
		const selectedCheckboxes = document.querySelectorAll('.item-checkbox:checked');

		if (!itemCheckboxes || !selectedCheckboxes) {
			console.warn('勾選框元素無法找到');
			return;
		}

		selectedCheckboxes.forEach((checkbox) => {
			const row = checkbox.closest('tr');
			const quantity = parseInt(row.querySelector('.quantity-input').value, 10);
			const price = parseFloat(row.querySelector('td:nth-child(2)').innerText.replace('$', ''));
			total += price * quantity;
			count++;
		});

		// 更新總價格與商品數量
		document.getElementById('selectedTotal').innerText = `$${Math.floor(total)}`;
		document.getElementById('selectedCount').innerText = count;

		const selectAllCart = document.getElementById('select-all-cart');
		if (selectAllCart) {
			selectAllCart.checked = itemCheckboxes.length === selectedCheckboxes.length;
		}

		// 更新每一組的全選框狀態
		document.querySelectorAll('.group-header').forEach((groupHeader) => {
			const group = groupHeader.nextElementSibling;
			const groupCheckboxes = group.querySelectorAll('.item-checkbox');
			const groupChecked = group.querySelectorAll('.item-checkbox:checked');
			const groupSelectAll = groupHeader.querySelector('.select-group');
			if (groupSelectAll) {
				groupSelectAll.checked = groupCheckboxes.length === groupChecked.length;
			}
		});
	} catch (error) {
		console.error('更新總計時發生錯誤：', error);
	}
}


// 全選功能
function selectAllCart(checkbox) {
	document.querySelectorAll('.item-checkbox, .select-group').forEach((cb) => (cb.checked = checkbox.checked));
	updateTotal();
}

// 分組全選功能
function selectAllGroup(groupCheckbox) {
	const group = groupCheckbox.closest('.group-header').nextElementSibling;
	group.querySelectorAll('.item-checkbox').forEach((checkbox) => (checkbox.checked = groupCheckbox.checked));
	updateTotal();
}

// 提示框
function showAlert(message) {
	const alertBox = document.createElement('div');
	alertBox.innerText = message;

	alertBox.style.position = 'fixed';
	alertBox.style.top = '20px';
	alertBox.style.left = '50%';
	alertBox.style.transform = 'translateX(-50%)';
	alertBox.style.backgroundColor = 'lightcoral';
	alertBox.style.color = 'white';
	alertBox.style.padding = '10px 20px';
	alertBox.style.borderRadius = '5px';
	alertBox.style.fontSize = '16px';
	alertBox.style.fontWeight = 'bold';
	alertBox.style.zIndex = '1000';
	alertBox.style.transition = 'opacity 0.5s ease';

	document.body.appendChild(alertBox);

	setTimeout(() => {
		alertBox.style.opacity = '0';
		setTimeout(() => alertBox.remove(), 500);
	}, 1500);
}

// 全局定義 toggleButtonState
function toggleButtonState(row, currentQuantity, maxNum) {
	const decreaseButton = row.querySelector('.decrease-btn');
	const increaseButton = row.querySelector('.increase-btn');

	decreaseButton.disabled = currentQuantity <= 1; // 當數量小於等於 1 時禁用減少按鈕
	increaseButton.disabled = currentQuantity >= maxNum; // 當數量大於等於最大數量時禁用增加按鈕
}


// 增加商品數量
function increaseQuantity(button) {
	const row = button.closest('tr'); // 找到當前行
	const quantityInput = row.querySelector('.quantity-input'); // 數量輸入框
	const maxNum = parseInt(row.getAttribute('data-max-num'), 10); // 獲取最大數量
	let currentQuantity = parseInt(quantityInput.value, 10); // 獲取當前數量

	if (maxNum === 1) {
		// 當最大值等於 1 時，直接提示
		showAlert('數量不能少於 1，且已達到商品的最大可購買數量！');
		return;
	}

	if (currentQuantity < maxNum) {
		currentQuantity++;
		quantityInput.value = currentQuantity; // 更新數量顯示

		const itemId = row.getAttribute('data-item-id'); // 商品 ID
		updateNumInDatabase(itemId, currentQuantity); // 更新數據庫
		updatePrice(row); // 更新該商品總價
		updateTotal(); // 更新總價和商品數量
	} else {
		showAlert('已達到商品的最大可購買數量！'); // 提示已達最大值
	}

	// 更新按鈕狀態
	toggleButtonState(row, currentQuantity, maxNum);
}





// 減少商品數量
function decreaseQuantity(button) {
	const row = button.closest('tr');
	const quantityInput = row.querySelector('.quantity-input');
	const maxNum = parseInt(row.getAttribute('data-max-num'), 10); // 最大數量
	let currentQuantity = parseInt(quantityInput.value, 10); // 當前數量

	if (maxNum === 1) {
		// 當最大值等於 1 時，直接提示
		showAlert('數量不能少於 1，且已達到商品的最大可購買數量！');
		return;
	}

	if (currentQuantity > 1) {
		currentQuantity--;
		quantityInput.value = currentQuantity; // 更新數量顯示

		const itemId = row.getAttribute('data-item-id'); // 商品 ID
		updateNumInDatabase(itemId, currentQuantity); // 更新數據庫
		updatePrice(row); // 更新單個商品的總價
		updateTotal(); // 更新總價
	} else {
		showAlert('數量不能少於 1！'); // 提示不能少於 1
	}

	// 更新按鈕狀態
	toggleButtonState(row, currentQuantity, maxNum);
}



// 存儲輸入框的初始值
function storePreviousValue(input) {
	input.dataset.previousValue = input.value; // 使用自定義屬性存儲當前值
}

// 驗證數量是否合法
function validateAndRestore(input) {
	const inputRow = input.closest('tr'); // 找到當前行
	const maxNum = parseInt(inputRow.getAttribute('data-max-num'), 10); // 獲取最大數量
	const minNum = parseInt(input.getAttribute('min'), 10); // 獲取最小數量
	const currentValue = parseInt(input.value, 10); // 用戶輸入的值
	const previousValue = input.dataset.previousValue || minNum; // 之前存儲的合法值

	// 檢查輸入是否超出範圍
	if (isNaN(currentValue) || currentValue < minNum || currentValue > maxNum) {
		showAlert(`輸入的數量必須在 ${minNum} 到 ${maxNum} 之間！`);
		input.value = previousValue; // 恢復到之前的合法值
		return;
	}

	// 如果合法，更新相關邏輯
	const row = input.closest('tr'); // 找到當前行
	const itemId = row.getAttribute('data-item-id'); // 假設行中有商品 ID
	updateNumInDatabase(itemId, currentValue); // 更新數據庫
	updatePrice(row); // 更新商品總價
	updateTotal(); // 更新購物車總價
}

// 發送更新數量請求
function updateNumInDatabase(itemId, newNum) {


	// 構建請求數據，確保 newNum 是數字
	const requestData = {
		itemId: parseInt(itemId, 10), // 確保為數字類型
		num: parseInt(newNum, 10),   // 確保為數字類型
	};

	console.log('發送的請求數據：', requestData); // 驗證數據


	const memId = 1; // 動態獲取會員 ID
	fetch(`/cart/${memId}/updateNum`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(requestData),
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('更新失敗');
			}
			return response.json();
		})
		.then((data) => {
			if (data.success) {
				console.log('數量更新成功');
			} else {
				console.error('更新失敗：', data.message);
			}
		})
		.catch((error) => {
			console.error('錯誤：', error);
			alert('無法更新數量，請稍後再試！');
		});
}

// 多筆移除功能
function removeSelectedItems() {
	const selectedItems = document.querySelectorAll('.item-checkbox:checked');
	if (selectedItems.length === 0) {
		alert('請先勾選要移除的商品！');
		return;
	}

	if (!confirm('確定要移除選中的商品嗎？')) {
		return;
	}

	const memId = 1; // 動態獲取會員 ID
	const itemIds = Array.from(selectedItems).map((checkbox) => {
		const row = checkbox.closest('tr');
		return parseInt(row.getAttribute('data-item-id'), 10);
	});

	fetch(`/cart/${memId}/removeSelectedItems`, {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({ itemIds }),
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('移除失敗');
			}
			return response.json();
		})
		.then((data) => {
			if (data.success) {
				console.log('批量移除成功');
				// 從 DOM 中移除已勾選的商品
				selectedItems.forEach((checkbox) => {
					const row = checkbox.closest('tr');
					const group = row.closest('.group');
					row.remove();

					// 如果該分組中已無商品，移除分組
					const remainingItems = group.querySelectorAll('tbody tr');
					if (remainingItems.length === 0) {
						group.remove();
					}
				});

				// 如果購物車完全為空，顯示空購物車提示
				const cartWrapper = document.querySelector('.cart-wrapper');
				if (!cartWrapper.querySelector('.group')) {
					document.querySelector('.cart-container').innerHTML = `
                        <div class="empty-cart">
                            <p>購物車為空</p>
                            <a href="/">現在就去逛逛!</a>
                        </div>
                    `;
				}

				updateTotal(); // 更新總價格與數量
			} else {
				console.error('移除失敗：', data.message);
			}
		})
		.catch((error) => {
			console.error('錯誤：', error);
			alert('無法移除商品，請稍後再試！');
		});
}




// 更新單個商品的總價格
function updatePrice(row) {
	const quantity = parseInt(row.querySelector('.quantity-input').value, 10);
	const price = parseFloat(row.querySelector('td:nth-child(2)').innerText.replace('$', ''));
	const totalCell = row.querySelector('td:nth-child(4)');
	totalCell.innerText = `$${Math.floor(price * quantity)}`;
}

// 防止按鈕頻繁觸發
document.querySelectorAll('.increase-btn, .decrease-btn').forEach((button) => {
	button.addEventListener('click', () => {
		button.disabled = true;
		setTimeout(() => (button.disabled = false), 100); // 0.1秒後恢復按鈕
	});
});

// 單個商品勾選功能
document.querySelectorAll('.item-checkbox').forEach((checkbox) => {
	checkbox.addEventListener('change', (event) => {
		const groupHeader = checkbox.closest('.group').querySelector('.group-header .select-group');
		const group = checkbox.closest('.group').querySelectorAll('.item-checkbox');
		const groupChecked = checkbox.closest('.group').querySelectorAll('.item-checkbox:checked');

		// 更新群組的勾選狀態
		groupHeader.checked = groupChecked.length === group.length;

		// 更新全選框狀態
		const selectAllCart = document.getElementById('select-all-cart');
		const allCheckboxes = document.querySelectorAll('.item-checkbox');
		const allChecked = document.querySelectorAll('.item-checkbox:checked');
		selectAllCart.checked = allCheckboxes.length === allChecked.length;

		updateTotal();
	});
});

// 結帳功能
// 收集購物車中已選中的商品資訊
function getSelectedCartItems() {
	const cartItems = document.querySelectorAll('.cart-container tbody tr');
	const selectedItems = [];

	cartItems.forEach(item => {
		const checkbox = item.querySelector('input[type="checkbox"]');
		if (checkbox && checkbox.checked) {
			// 提取商品相關資訊
			const cartId = parseInt(item.getAttribute('data-cart-id'), 10);
			const itemId = parseInt(item.getAttribute('data-item-id'), 10);
			const cafeId = parseInt(item.getAttribute('data-cafe-id'), 10);
			const price = parseFloat(item.querySelector('.item-totalprice').innerText);
			const num = parseInt(item.querySelector('.quantity-input').value, 10);

			// 將商品資訊加入陣列
			selectedItems.push({
				cartId, // 購物車 ID
				itemId, // 商品 ID
				cafeId, // 咖啡廳 ID
				num,    // 數量
				price   // 單價
			});
		}
	});

	return selectedItems;
}

// 結帳功能
function checkout() {
	// 收集選中的商品資料
	const selectedItems = getSelectedCartItems();

	if (selectedItems.length === 0) {
		alert("請選擇至少一個商品進行結帳！");
		return;
	}

	const requestData = {
		memId: 1, // 假設從 sessionStorage 或後端獲取會員 ID
		itemIds: selectedItems.map(item => item.itemId) // 僅傳送 itemId
	};

	console.log("發送的請求數據：", requestData);

	fetch('/cart/checkout', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(requestData)
	})
		.then(response => {
			if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
			return response.json();
		})
		.then(data => {
			console.log("結帳成功：", data);
			// 跳轉到頁面渲染的 URL，並傳遞參數
			const queryString = `memId=${requestData.memId}&selectedItemIds=${requestData.itemIds.join(',')}`;
			window.location.href = `/cart/checkout/page?${queryString}`;
		})
		.catch(error => {
			console.error("結帳失敗：", error.message);
			alert(`結帳失敗：${error.message}`);
		});
}
