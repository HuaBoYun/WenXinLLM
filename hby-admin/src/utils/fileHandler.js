import axios from 'axios';
import CryptoJS from 'crypto-js';
// 将Base64字符串转换为Uint8Array
function base64ToUint8Array(base64String) {
  const binaryString = atob(base64String); // 解码Base64字符串为二进制字符串
  const len = binaryString.length;
  const bytes = new Uint8Array(len);

  for (let i = 0; i < len; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }

  return bytes;
}

// 创建并触发下载链接
function createAndTriggerDownloadLink(blob, attname) {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.style.display = 'none';
  link.href = url;
  link.download = attname;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
}

// 获取文件数据用于批量下载（不直接触发下载）
export async function getFileData(row, headers, lodeapi, key, iv) {
  try {
    if (!row || !row.attid) {
      throw new Error('文件 ID 不存在');
    }

    console.log(`准备获取文件数据 ${row.attname}`);

    // 检查文件是否加密
    if (row.isEncrypted && row.jmurl) {
      // 对于加密文件，使用jmurl进行下载
      console.log('获取加密文件数据，使用jmurl');
      const decryptedUrl = atob(row.jmurl); // 解码base64编码的URL
      const response = await axios.get(decryptedUrl, {
        headers: headers,
        responseType: 'blob', // 直接获取blob数据
      });

      return response.data; // 返回blob数据
    }

    // 对于非加密文件，首先尝试获取blob数据
    const params = new URLSearchParams({ fileId: row.attid }).toString();

    // 首先尝试以blob方式获取，检查是否为直接二进制数据
    const blobResponse = await axios.get(`${lodeapi}?${params}`, {
      headers: headers,
      responseType: 'blob',
    });

    // 检查响应内容类型
    const contentType = blobResponse.headers['content-type'];
    console.log('响应内容类型:', contentType);

    // 如果是二进制流数据（如Excel、PDF等），直接返回
    if (contentType && (contentType.includes('application/octet-stream') ||
      contentType.includes('application/vnd') ||
      contentType.includes('application/pdf') ||
      contentType.includes('application/msword') ||
      contentType.includes('application/zip'))) {
      console.log('检测到二进制文件，直接返回blob数据');
      return blobResponse.data;
    }

    // 否则，尝试作为Base64加密数据处理
    console.log('尝试作为Base64加密数据处理');
    const textResponse = await axios.get(`${lodeapi}?${params}`, {
      headers: headers,
      responseType: 'text',
    });

    if (textResponse.data.length === 0) {
      console.error('服务器返回的响应数据为空');
      throw new Error('服务器返回的响应数据为空');
    }

    // 将Base64字符串转换为Uint8Array
    let encryptedUint8Array;
    try {
      encryptedUint8Array = base64ToUint8Array(textResponse.data);
    } catch (base64Error) {
      console.log('Base64解码失败，直接返回blob数据');
      // 如果Base64解码失败，直接返回blob数据
      return blobResponse.data;
    }

    // AES解密参数
    const secretKey = CryptoJS.enc.Utf8.parse('b8e9a1c7d4f265a830e7b1f4d8a9c6e2'); // AES解密密钥
    const ivParsed = CryptoJS.enc.Utf8.parse('a1b2c3d4e5f6g7h8'); // 初始化向量 (IV)

    // 使用CryptoJS解密
    const decryptedData = CryptoJS.AES.decrypt(
      { ciphertext: CryptoJS.lib.WordArray.create(encryptedUint8Array.buffer) },
      secretKey,
      {
        iv: ivParsed,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      }
    );

    // 将解密后的数据转换为Uint8Array
    const decryptedBytes = new Uint8Array(decryptedData.sigBytes);
    const decryptedWords = decryptedData.words;

    for (let i = 0; i < decryptedWords.length; i++) {
      const word = decryptedWords[i];
      decryptedBytes[i * 4] = (word >> 24) & 0xff;
      decryptedBytes[i * 4 + 1] = (word >> 16) & 0xff;
      decryptedBytes[i * 4 + 2] = (word >> 8) & 0xff;
      decryptedBytes[i * 4 + 3] = word & 0xff;
    }

    // MIME类型确定
    const mimeType = row.mimeType || 'application/octet-stream'; // 默认使用application/octet-stream

    // 创建Blob对象并返回
    const decryptedBlob = new Blob([decryptedBytes], { type: mimeType });
    return decryptedBlob;

  } catch (error) {
    console.error('获取文件数据时出错:', error);
    throw error;
  }
}

export async function handleDown(row, headers, lodeapi, key, iv) {
  try {
    if (!row || !row.attid) {
      throw new Error('文件 ID 不存在');
    }

    console.log(`准备下载文件 ${row.attname}`);

    // 检查文件是否加密
    if (row.isEncrypted && row.jmurl) {
      // 对于加密文件，使用jmurl进行下载
      console.log('下载加密文件，使用jmurl');
      const decryptedUrl = atob(row.jmurl); // 解码base64编码的URL
      const response = await axios.get(decryptedUrl, {
        headers: headers,
        responseType: 'blob', // 直接获取blob数据
      });

      // 直接创建下载链接
      let originalFilename = row.attname || row.trainevidence || 'downloaded_file';
      createAndTriggerDownloadLink(response.data, originalFilename);
      return;
    }

    // 对于非加密文件，首先尝试获取blob数据
    const params = new URLSearchParams({ fileId: row.attid }).toString();

    // 首先尝试以blob方式获取，检查是否为直接二进制数据
    const blobResponse = await axios.get(`${lodeapi}?${params}`, {
      headers: headers,
      responseType: 'blob',
    });

    // 检查响应内容类型
    const contentType = blobResponse.headers['content-type'];
    console.log('响应内容类型:', contentType);

    // 如果是二进制流数据（如Excel、PDF等），直接下载
    if (contentType && (contentType.includes('application/octet-stream') ||
      contentType.includes('application/vnd') ||
      contentType.includes('application/pdf') ||
      contentType.includes('application/msword') ||
      contentType.includes('application/zip'))) {
      console.log('检测到二进制文件，直接下载');
      let originalFilename = row.attname || 'downloaded_file';
      createAndTriggerDownloadLink(blobResponse.data, originalFilename);
      return;
    }

    // 否则，尝试作为Base64加密数据处理
    console.log('尝试作为Base64加密数据处理');
    const textResponse = await axios.get(`${lodeapi}?${params}`, {
      headers: headers,
      responseType: 'text',
    });

    if (textResponse.data.length === 0) {
      console.error('服务器返回的响应数据为空');
      return;
    }

    // 将Base64字符串转换为Uint8Array
    let encryptedUint8Array;
    try {
      encryptedUint8Array = base64ToUint8Array(textResponse.data);
    } catch (base64Error) {
      console.log('Base64解码失败，尝试直接下载blob数据');
      // 如果Base64解码失败，直接使用blob数据
      let originalFilename = row.attname || 'downloaded_file';
      createAndTriggerDownloadLink(blobResponse.data, originalFilename);
      return;
    }

    // AES解密参数
    const secretKey = CryptoJS.enc.Utf8.parse('b8e9a1c7d4f265a830e7b1f4d8a9c6e2'); // AES解密密钥
    const ivParsed = CryptoJS.enc.Utf8.parse('a1b2c3d4e5f6g7h8'); // 初始化向量 (IV)

    // 使用CryptoJS解密
    const decryptedData = CryptoJS.AES.decrypt(
      { ciphertext: CryptoJS.lib.WordArray.create(encryptedUint8Array.buffer) },
      secretKey,
      {
        iv: ivParsed,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      }
    );

    // 将解密后的数据转换为Uint8Array
    const decryptedBytes = new Uint8Array(decryptedData.sigBytes);
    const decryptedWords = decryptedData.words;

    for (let i = 0; i < decryptedWords.length; i++) {
      const word = decryptedWords[i];
      decryptedBytes[i * 4] = (word >> 24) & 0xff;
      decryptedBytes[i * 4 + 1] = (word >> 16) & 0xff;
      decryptedBytes[i * 4 + 2] = (word >> 8) & 0xff;
      decryptedBytes[i * 4 + 3] = word & 0xff;
    }

    // MIME类型确定
    const mimeType = row.mimeType || 'application/octet-stream'; // 默认使用application/octet-stream

    // 创建Blob对象
    const decryptedBlob = new Blob([decryptedBytes], { type: mimeType });

    // 文件名处理
    let originalFilename = row.attname ? row.attname.replace('.enc', '') : 'downloaded_file';
    console.log(`创建下载链接: ${originalFilename}`);
    createAndTriggerDownloadLink(decryptedBlob, originalFilename);

  } catch (error) {
    console.error('处理下载时出错:', error);
    throw error;
  }
}
