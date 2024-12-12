// 提示框功能
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

// 同步數量到隱藏的表單字段
function syncFormQuantity(input) {
    const row = input.closest('tr');
    const formQuantityInput = row.querySelector('.form-quantity');
    formQuantityInput.value = input.value; // 同步數量到表單
}

// 切換增加/減少按鈕的狀態
function toggleButtonState(row, currentQuantity, maxNum) {
    const decreaseButton = row.querySelector('.decrease-btn');
    const increaseButton = row.querySelector('.increase-btn');

    decreaseButton.disabled = currentQuantity <= 1; // 當數量 <= 1 時禁用減少按鈕
    increaseButton.disabled = currentQuantity >= maxNum; // 當數量 >= 最大數量時禁用增加按鈕
}

// 增加商品數量
function increaseQuantity(button) {
    const row = button.closest('tr');
    const quantityInput = row.querySelector('.quantity-input');
    const maxNum = parseInt(row.dataset.maxNum, 10); // 最大數量
    let currentQuantity = parseInt(quantityInput.value, 10); // 當前數量

    if (currentQuantity < maxNum) {
        currentQuantity++;
        quantityInput.value = currentQuantity; // 更新數量顯示
        syncFormQuantity(quantityInput); // 同步數量到表單
    } else {
        showAlert('已達到商品的最大可購買數量！');
    }

    toggleButtonState(row, currentQuantity, maxNum);
}

// 減少商品數量
function decreaseQuantity(button) {
    const row = button.closest('tr');
    const quantityInput = row.querySelector('.quantity-input');
    let currentQuantity = parseInt(quantityInput.value, 10); // 當前數量

    if (currentQuantity > 1) {
        currentQuantity--;
        quantityInput.value = currentQuantity; // 更新數量顯示
        syncFormQuantity(quantityInput); // 同步數量到表單
    } else {
        showAlert('數量不能少於 1！');
    }

    const maxNum = parseInt(row.dataset.maxNum, 10); // 最大數量
    toggleButtonState(row, currentQuantity, maxNum);
}

// 驗證輸入數量是否合法
function validateAndRestore(input) {
    const row = input.closest('tr');
    const maxNum = parseInt(row.dataset.maxNum, 10); // 最大數量
    const minNum = parseInt(input.getAttribute('min'), 10); // 最小數量
    const currentValue = parseInt(input.value, 10); // 當前值
    const previousValue = input.dataset.previousValue || minNum; // 之前存儲的合法值

    if (isNaN(currentValue) || currentValue < minNum || currentValue > maxNum) {
        showAlert(`輸入的數量必須在 ${minNum} 到 ${maxNum} 之間！`);
        input.value = previousValue; // 恢復到之前的合法值
    }

    syncFormQuantity(input); // 同步數量到表單
    const currentQuantity = parseInt(input.value, 10); // 獲取當前數量
    toggleButtonState(row, currentQuantity, maxNum);
}

// 初始化按鈕狀態
document.addEventListener('DOMContentLoaded', () => {
    const rows = document.querySelectorAll('tr[data-max-num]');
    rows.forEach(row => {
        const quantityInput = row.querySelector('.quantity-input');
        const currentQuantity = parseInt(quantityInput.value, 10); // 當前數量
        const maxNum = parseInt(row.dataset.maxNum, 10); // 最大數量
        toggleButtonState(row, currentQuantity, maxNum); // 初始化按鈕狀態
    });
});

// 防止按鈕頻繁觸發
document.querySelectorAll('.increase-btn, .decrease-btn').forEach(button => {
    button.addEventListener('click', () => {
        button.disabled = true;
        setTimeout(() => (button.disabled = false), 100); // 0.1秒後恢復按鈕
    });
});
