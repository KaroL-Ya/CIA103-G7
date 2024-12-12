// 清除提示訊息
function hideContent(d) {
	document.getElementById(d).style.display = "none";
}

// 照片上傳預覽
const filereader_support = typeof FileReader !== 'undefined';
if (!filereader_support) {
	alert("您的瀏覽器不支援 FileReader，請更新您的瀏覽器！");
}

const acceptedTypes = {
	'image/png': true,
	'image/jpeg': true,
	'image/gif': true
};

document.addEventListener("DOMContentLoaded", function() {
	const fileInput = document.getElementById("coverImg");

	if (fileInput) {
		fileInput.addEventListener("change", function(event) {
			const files = event.target.files || event.dataTransfer.files;
			for (let i = 0; i < files.length; i++) {
				previewFile(files[i]);
			}
		});
	}
});

function previewFile(file) {
	const blobHolder = document.getElementById("blob_holder");

	if (filereader_support && acceptedTypes[file.type]) {
		const reader = new FileReader();
		reader.onload = function(event) {
			const image = new Image();
			image.src = event.target.result;
			image.width = 90; // 調整圖片寬度
			image.height = 120; // 調整圖片高度
			image.style.marginTop = "5px";

			// 清空並新增圖片預覽
			blobHolder.innerHTML = "";
			blobHolder.appendChild(image);
		};
		reader.readAsDataURL(file);
		document.getElementById("submit").disabled = false;
	} else {
		blobHolder.innerHTML = `
			<div style="text-align: left; color: red;">
				<p>● 檔案名稱: ${file.name}</p>
				<p>● 檔案類型: ${file.type}</p>
				<p>● 檔案大小: ${file.size} bytes</p>
				<p>● 僅支援: <b>image/png、image/jpeg、image/gif</b></p>
			</div>`;
		document.getElementById("submit").disabled = true;
	}
}
