# ============================================================
# setting 目录下的 properties 文件含有敏感信息
# 这些文件已加入 .gitignore，不会提交到 git
# 请联系运维人员获取各环境的真实配置文件
# ============================================================
#
# 文件列表：
#   fileftp.properties    - 默认FTP配置（ZH环境）
#   fileftpDQ.properties  - 客户I环境FTP配置
#   fileftpHB.properties  - 华博环境FTP配置
#   fileftpHG.properties  - 航港环境FTP配置
#   fileftpXW.properties  - 新网环境FTP配置
#   fileftpZH.properties  - 中核环境FTP配置
#   fileftpZZ.properties  - 中州环境FTP配置
#   humanResources.properties - 人事系统配置
#   landray.properties    - 蓝凌OA配置
#   mainDataResources.properties - 主数据平台配置
#   process.properties    - 工作流配置
#   qyweixin.properties   - 企业微信配置
#   sxjm.properties       - 山西焦煤接口配置
#   yy.properties         - 用友云API配置
#
# ============================================================
# fileftp*.properties 通用结构示例：
# ============================================================
#
# uploadfilepath=/opt/ftp/uploadfile/
# sengfilepath=/opt/ftp/sengfile/
# xmlfilepath=/opt/ftp/xmlfile/
# templatefilepath=/opt/ftp/templatefile/
# constractfilePath=/opt/ftp/constract/
# systemImgFilePath=/opt/ftp/formimg/
# pythonFlfgPath=/opt/ftp/python/flfg/
# openOfficePath=C:/Program Files (x86)/OpenOffice 4/
# openOfficeip=127.0.0.1
# openOfficeport=8100
# ftpip=your_ftp_ip
# username=your_ftp_username
# password=your_ftp_password
# port=21
# previewurl=http://your-preview-server:8012/onlinePreview?url=
# secret:your_secret_key
# uploadpath:/opt/ftp/uploadfile/
# downloadUrl:http://your-download-server/api/file/file/download/decrypt?fileId=
# ssqHost=https://api.bestsign.info
# clientId=your_bestsign_client_id
# clientSecret=your_bestsign_client_secret
# privateKey=your_rsa_private_key_pkcs8_base64
#
# ============================================================
# qyweixin.properties 结构示例：
# ============================================================
#
# qyweixin_corp_id=your_corp_id
# qyweixin_corp_secret=your_corp_secret
# qyweixin_agent_id=your_agent_id
# qyweixin_url_prefix=https://qyapi.weixin.qq.com/cgi-bin
# qyweixin_get_token=/gettoken?corpid=CORPID&corpsecret=CORPSECRET
# qyweixin_message_send=/message/send?access_token=ACCESS_TOKEN
# call_back_url_prefix=http://your-callback-domain
# setup_service_url=http://your-service-url:8763
#
# ============================================================
# humanResources.properties 结构示例：
# ============================================================
#
# dev_account=your_hr_account_token
# dev_password=your_hr_dev_password
# tokenUrl=http://your-hr-server/api/auth.do?method=token
# serverUrl=http://your-hr-server/
# phone=your_phone_number
# OaLoginUrl=http://your-oa-server/seeyon/thirdpartyController.do?ticket=
# oaUrl=http://your-oa-server/
# oaCode= 
# loginUrl=http://your-system-url/setting/
# restpassword= 
#
# ============================================================
# mainDataResources.properties 结构示例：
# ============================================================
#
# mainDataIP=http://your-maindata-server:9999
# bigdataToken= 
# IntegrationKey= 
