import axios from 'axios';
import CryptoJS from 'crypto-js';

class FileUploader {
  constructor(baseApi, api, headers, key, iv) {
    this.baseApi = baseApi;
    this.api = api;
    this.headers = headers;
    this.key = CryptoJS.enc.Utf8.parse(key); // 确保密钥是WordArray格式
    this.iv = CryptoJS.enc.Utf8.parse(iv); // 初始化向量 (IV)
  }

  async uploadFile(file, options) {
    try {
      const reader = new FileReader();
      reader.readAsArrayBuffer(file);

      reader.onload = async (event) => {
        const arrayBuffer = event.target.result;
        const uint8Array = new Uint8Array(arrayBuffer);
        const wordArray = CryptoJS.lib.WordArray.create(uint8Array.buffer);

        // 使用AES CBC模式进行加密
        const encryptedData = CryptoJS.AES.encrypt(wordArray, this.key, { iv: this.iv });

        // 将加密后的数据转换为Base64字符串
        const base64String = encryptedData.ciphertext.toString(CryptoJS.enc.Base64);

        // 创建Blob对象
        const blob = new Blob([base64String], { type: 'application/octet-stream' });
        const encryptedFile = new File([blob], `${file.name}.enc`, { type: 'application/octet-stream' });

        const formData = new FormData();
        formData.append('file', encryptedFile);

        // 添加额外的表单数据
        if (options.formData) {
          Object.keys(options.formData).forEach(key => {
            formData.append(key, options.formData[key]);
          });
        }

        const response = await axios.post(this.api, formData, {
          headers: {
            ...this.headers,
            'Content-Type': 'multipart/form-data',
          },
          onUploadProgress: progressEvent => {
            const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
            if (typeof options.onProgress === 'function') {
              options.onProgress({ percent: percentCompleted, filename: file.name });
            }
          },
        });

        console.log('服务器响应:', response.data);

        if (typeof options.onSuccess === 'function') {
          options.onSuccess(response.data, file.name);
        }
      };

      reader.onerror = (error) => {
        console.error('读取文件出错:', error);
        if (typeof options.onError === 'function') {
          options.onError(error);
        }
      };
    } catch (error) {
      console.error('上传过程中出错:', error);

      if (typeof options.onError === 'function') {
        options.onError(error, file.name);
      }
    }
  }
}

export function customUpload(options) {
  console.log("开始自定义上传:", options);

  const uploader = new FileUploader(options.baseApi, options.api, options.headers, options.key, options.iv);

  // 确保 fileList 是一个数组
  let files = Array.isArray(options.fileList) ? options.fileList : [options.file];

  if (files.length === 0) {
    console.error('没有文件需要上传');
    if (typeof options.onError === 'function') {
      options.onError(new Error('没有文件需要上传'));
    }
    return;
  }

  // 遍历所有文件并上传
  files.forEach(file => {
    uploader.uploadFile(file, {
      formData: options.formData, // 传递额外的表单数据
      onProgress: options.onProgress,
      onSuccess: options.onSuccess,
      onError: options.onError
    });
  });
}