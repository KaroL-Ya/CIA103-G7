document.addEventListener('DOMContentLoaded', function() {

	// 計算每間咖啡廳的商品總金額
	function calculateCafeTotal() {
		let cafeGroups = document.querySelectorAll('.cafe-group');
		let totalProductAmount = 0;
		let totalShippingAmount = 0;
		let totalCouponDiscount = 0;
		let platformCouponValue = parseInt(document.querySelector('#platform-coupon').value);  // 平台優惠券金額

		// 遍歷每間咖啡廳
		cafeGroups.forEach(function(cafeGroup) {
			let cafeProductTotal = 0;
			let shippingFee = 100;  // 假設每間咖啡廳運費是100元
			let couponValue = parseInt(cafeGroup.querySelector('.cafe-coupon').value);  // 咖啡廳優惠券金額

			// 計算每間咖啡廳的商品總金額
			let items = cafeGroup.querySelectorAll('tbody tr');
			items.forEach(function(itemRow) {
				let itemTotalPrice = parseInt(itemRow.querySelector('td:nth-child(4)').textContent.replace(' 元', ''));
				cafeProductTotal += itemTotalPrice;
			});

			// 計算咖啡廳最終金額（商品總金額 - 優惠券 + 運費）
			let finalCafeTotal = cafeProductTotal - couponValue + shippingFee;
			cafeGroup.querySelector('.cafe-total-amount').textContent = cafeProductTotal.toLocaleString() + ' 元';
			cafeGroup.querySelector('.final-cafe-total').textContent = finalCafeTotal.toLocaleString() + ' 元';

			// 更新平台的總金額
			totalProductAmount += cafeProductTotal;
			totalShippingAmount += shippingFee;
			totalCouponDiscount += couponValue;
		});

		// 計算平台總金額
		let totalAmountAfterPlatformCoupon = totalProductAmount + totalShippingAmount - totalCouponDiscount - platformCouponValue;

		// 更新付款詳情
		document.querySelector('#total-product-amount').textContent = totalProductAmount.toLocaleString() + ' 元';
		document.querySelector('#total-shipping-amount').textContent = totalShippingAmount.toLocaleString() + ' 元';
		document.querySelector('#total-coupon-discount').textContent = totalCouponDiscount.toLocaleString() + ' 元';
		document.querySelector('#total-payment-amount').textContent = totalAmountAfterPlatformCoupon.toLocaleString() + ' 元';

		// 更新footer中的總付款金額
		updateFooterTotalAmount();
	}

	// 計算頁面載入後即時更新
	calculateCafeTotal();

	// 監聽每間咖啡廳的優惠券變動
	let couponSelects = document.querySelectorAll('.cafe-coupon');
	couponSelects.forEach(function(select) {
		select.addEventListener('change', calculateCafeTotal);
	});

	// 監聽平台優惠券變動
	document.querySelector('#platform-coupon').addEventListener('change', calculateCafeTotal);

	// 更新footer中的總付款金額
	function updateFooterTotalAmount() {
		const totalPaymentAmount = document.querySelector('#total-payment-amount').textContent;
		document.querySelector('.footer #total-payment-amount').textContent = totalPaymentAmount;
	}

	// 確保 Footer 不會遮擋底部商品
	window.addEventListener('load', () => {
		const footerHeight = document.querySelector('.footer').offsetHeight;
		document.querySelector('.checkout-container').style.marginBottom = `${footerHeight}px`;
	});

	window.submitOrder = function() {
		let orderData = {
			cafes: [],
			platformCoupon: parseInt(document.querySelector('#platform-coupon').value) || 0,
			memId: 1001,
		};

		let cafeGroups = document.querySelectorAll('.cafe-group');
		cafeGroups.forEach(function(cafeGroup) {
			let cafe = {
				cafeId: parseInt(cafeGroup.querySelector('.cafe-id').value),
				items: [],
				shippingFee: 100,
				remark: cafeGroup.querySelector('.cafe-remark input')?.value || '',
			};

			let items = cafeGroup.querySelectorAll('tbody tr');
			items.forEach(function(itemRow) {
				let itemId = itemRow.getAttribute('data-item-id');
				let itemName = itemRow.querySelector('td:nth-child(1)').textContent;
				let quantity = parseInt(itemRow.querySelector('td:nth-child(2)').textContent, 10);
				let price = parseInt(itemRow.querySelector('td:nth-child(3)').textContent.replace('元', '').trim(), 10);

				cafe.items.push({ itemId, itemName, num: quantity, price });
			});

			orderData.cafes.push(cafe);
		});

		// 保存訂單資料到 localStorage
		localStorage.setItem("orderData", JSON.stringify(orderData));
		console.log(localStorage.getItem("orderData"));


		fetch("/cart/submit-order", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(orderData),
		})
			.then((response) => response.text()) // 後端返回的是 HTML 表單
			.then((htmlForm) => {
				// 動態創建一個容器，用來插入 HTML 表單
				let formContainer = document.createElement('div');
				formContainer.innerHTML = htmlForm;
				document.body.appendChild(formContainer);

				// 自動提交表單
				formContainer.querySelector('form').submit();
			})
			.catch((error) => console.error("提交訂單失敗:", error));
	};

});
