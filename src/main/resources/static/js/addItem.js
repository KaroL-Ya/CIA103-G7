document.addEventListener("DOMContentLoaded", function () {
    const fileInput = document.getElementById("coverImg");
    const blobHolder = document.getElementById("blob_holder");

    fileInput.addEventListener("change", function () {
        const files = fileInput.files;
        blobHolder.innerHTML = ""; // 清空現有預覽

        if (files.length > 0) {
            const file = files[0];
            if (file.type.startsWith("image/")) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const image = new Image();
                    image.src = e.target.result;
                    image.style.height = "120px";
                    image.style.width = "auto"; // 保持比例
                    blobHolder.appendChild(image);
                };
                reader.readAsDataURL(file);
            } else {
                blobHolder.innerHTML = "請上傳有效的圖片檔案";
            }
        }
    });
});
