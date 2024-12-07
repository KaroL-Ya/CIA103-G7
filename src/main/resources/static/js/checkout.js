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

});
