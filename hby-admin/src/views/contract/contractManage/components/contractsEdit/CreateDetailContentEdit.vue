<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="form"
        :disabled="disabled"
        label-width="150px"
        :model="formData"
        :rules="rules"
        :append-to-body="true"
      >
        <el-col :span="24">
          <el-divider>合同基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <el-input
              v-model="formData.contractno"
              clearable
              placeholder="请输入合同编号"
              readonly
              :disabled="true"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item ref="contractname" label="合同名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="topicname">
            <el-input
              v-model="formData.topicname"
              clearable
              readonly
              placeholder="请选择项目名称"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.setup.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同类型" prop="contracttype">
            <el-input
              v-model="formData.contracttype"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同分类" prop="typefl">
            <el-input
              v-model="formData.typefl"
              placeholder
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="formFields['startdate'].label" prop="startdate">
            <el-date-picker
              v-model="formData.startdate"
              clearable
              :placeholder="`请输入${formFields['startdate'].label}`"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="formFields['enddate'].label" prop="enddate">
            <el-date-picker
              v-model="formData.enddate"
              clearable
              :placeholder="`请输入${formFields['enddate'].label}`"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item
            :label="formFields['budgetname'].label"
            prop="budgetname"
          >
            <el-input
              v-model="formData.budgetname"
              clearable
              readonly
              :placeholder="`请选择${formFields['budgetname'].label}`"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xdf.show('HTGL001', 'budgetname')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col v-if="formFields['counterparttype']" :span="12">
          <el-form-item
            :label="formFields['counterparttype'].label"
            prop="counterparttype"
          >
            <el-input
              v-model="formData.counterparttype"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['director']" :span="12">
          <el-form-item :label="formFields['director'].label" prop="director">
            <el-input
              v-model="formData.director"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['counterpartaddress']" :span="12">
          <el-form-item
            :label="formFields['counterpartaddress'].label"
            prop="counterpartaddress"
          >
            <el-input
              v-model="formData.counterpartaddress"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['contacts']" :span="12">
          <el-form-item :label="formFields['contacts'].label" prop="contacts">
            <el-input
              v-model="formData.contacts"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['contactsphone']" :span="12">
          <el-form-item
            :label="formFields['contactsphone'].label"
            prop="contactsphone"
          >
            <el-input
              v-model="formData.contactsphone"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['contractbd']" :span="12">
          <el-form-item
            :label="formFields['contractbd'].label"
            prop="contractbd"
          >
            <el-input
              v-model="formData.contractbd"
              clearable
              :placeholder="`请输入${formFields['contractbd'].label}`"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col v-if="formFields['contractzd']" :span="12" style="height: 50.5px">
          <el-form-item :label="formFields['contractzd'].label" prop="contractzd">
            <template v-if="currentEdit == 'moren'">
              <el-select v-model="formData.contractzd" placeholder="请选择合同层级">
                <el-option
                  v-for="item in contractLevelOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </template>
            <template v-else>
              <el-input
                v-model="formData.contractzd"
                clearable
                :placeholder="`请输入${formFields['contractzd'].label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="银行账号" prop="bankaccount">
            <el-input
              v-model="formData.bankaccount"
              clearable
              readonly
              placeholder="请选择银行账号"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleChoseBankClick"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户银行" prop="bankkhyh">
            <el-input
              v-model="formData.bankkhyh"
              placeholder="请输入开户银行"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col
          v-if="formFields['contractdatetype']"
          :span="12"
          style="height: 51px"
        >
          <el-form-item
            :label="formFields['contractdatetype'].label"
            prop="contractdatetype"
          >
            <template v-if="currentEdit == 'moren'">
              <el-radio-group v-model="formData.contractdatetype">
                <el-radio label="固定期限">固定期限</el-radio>
                <el-radio label="无固定期限">无固定期限</el-radio>
              </el-radio-group>
            </template>
            <template v-else>
              <el-input
                v-model="formData.contractdatetype"
                clearable
                :placeholder="`请输入${formFields['contractdatetype'].label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col>

        <el-col
          v-if="formFields['contractplan']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contractplan'].label"
            prop="contractplan"
          >
            <el-radio-group v-model="formData.contractplan">
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col
          v-if="formFields['contractxz']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contractxz'].label"
            prop="contractxz"
          >
            <template v-if="currentEdit == 'moren'">
              <el-radio-group v-model="formData.contractxz">
                <el-radio label="初始合同">初始合同</el-radio>
                <el-radio label="补充合同">补充合同</el-radio>
              </el-radio-group>
            </template>
            <template v-else>
              <el-input
                v-model="formData.contractxz"
                clearable
                :placeholder="`请输入${formFields['contractxz'].label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['contractlink']" :span="12">
          <el-form-item
            :label="formFields['contractlink'].label"
            prop="contractlink"
          >
            <el-input
              v-model="formData.contractlink"
              :disabled="formFields['contractlink'].disabled"
              :placeholder="`请输入${formFields['contractlink'].label}`"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- jijiatype字段在金额信息部分也有用，在非默认模板情况下才用在合同基本信息 -->
        <el-col
          v-if="currentEdit !== 'moren' && formFields['jijiatype']"
          :span="12"
        >
          <el-form-item :label="formFields['jijiatype'].label" prop="jijiatype">
            <el-input
              v-model="formData.jijiatype"
              :disabled="formFields['jijiatype'].disabled"
              :placeholder="`请输入${formFields['jijiatype'].label}`"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方签署主体" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              readonly
              placeholder="请选择我方签署主体"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.unit.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="承办部门" prop="orgmeno">
            <el-input
              v-model="formData.orgmeno"
              clearable
              readonly
              placeholder="请选择承办部门"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="承办人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              readonly
              placeholder="请选择承办人"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="executorShow('')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="授权委托人" prop="entrustStaffName">
            <el-input
              v-model="formData.entrustStaffName"
              clearable
              disabled
              placeholder="请选择授权委托人"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="executorShow('entrustStaff')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目名称" prop="topicname">
            <el-input
              v-model="formData.topicname"
              clearable
              placeholder="请选择项目名称"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.setup.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <template v-if="formFields['contractchildren']">
          <el-col :span="12" style="height: 50.5px">
            <el-form-item label="是否关联合同" prop="contractchildren">
              <el-radio-group v-model="formData.contractchildren">
                <el-radio label="是">是</el-radio>
                <el-radio label="否">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col v-show="formData['contractchildren'] == '是'" :span="12">
            <el-form-item label="关联合同" prop="parentname">
              <el-input
                v-model="formData.parentname"
                clearable
                readonly
                placeholder="请选择关联合同"
                :style="{ width: '80%' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.xdf.show('HTGL005', 'parentname')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="12">
          <el-form-item label="是否为多个合同" prop="ismany">
            <el-radio-group v-model="formData.ismany">
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.ismany == '是'">
          <el-form-item label="多个合同数量" prop="agreementcount">
            <el-input-number
              v-model="formData.agreementcount"
              clearable
              placeholder="请输入合同数量"
              :style="{ width: '256px' }"
              :min="1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="formFields['momoconcat'].label">
            <el-input
              v-model="formData.momoconcat"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :placeholder="`请输入${formFields['momoconcat'].label}`"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['riskcontrol']" :span="24">
          <el-form-item :label="formFields['riskcontrol'].label">
            <el-input
              v-model="formData.riskcontrol"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :placeholder="`请输入${formFields['riskcontrol'].label}`"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>合同金额信息</el-divider>
        </el-col>
        <el-col :span="12" style="height: 50px">
          <el-form-item label="收付方向" prop="dctype">
            <el-radio-group v-model="formData.dctype" @change="dctypeChange">
              <el-radio label="收款">收款</el-radio>
              <el-radio label="付款">付款</el-radio>
              <el-radio label="无">无</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="formFields['contractmoney'].label"
            prop="contractmoney"
          >
            <el-input
              v-model="formData.contractmoney"
              clearable
              :placeholder="`请输入${formFields['contractmoney'].label}`"
              :style="{ width: '100%' }"
              type="number"
              @blur="handleBlur"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="currentEdit == 'moren'" :span="12" style="height: 50px">
          <el-form-item label="计价方式" prop="jijiatype">
            <el-radio-group
              v-model="formData.jijiatype"
              @change="jijiatypeChange"
            >
              <el-radio label="固定总价">固定总价</el-radio>
              <el-radio label="非固定总价">非固定总价</el-radio>
              <el-radio label="无金额">无金额</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="moneytype">
            <el-input
              v-model="formData.moneytype"
              clearable
              placeholder="请输入币种"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人民币大写" prop="hzsumowing">
            <el-input
              v-model="formData.hzsumowing"
              clearable
              disabled
              placeholder=""
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="showSubFields" :span="24">
          <el-divider>{{ subTitle }}</el-divider>
          <SubList
            :contract="formData"
            :fields="subFields"
            :show-total="formData.contracttype == '产品销售合同'"
            @data-change="formData.informationList = $event"
          />
        </el-col>
        <el-col>
          <el-divider>相对方信息</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="handleAdds">增加一行</el-button>
          </div>
          <!-- 新增可编辑表格 -->
          <el-table :data="tableDataProject">
            <el-table-column align="center" label="相对方类型" prop="bugetType">
              <template slot-scope="scope">
                <el-select placeholder="请选择" v-model="scope.row.bugetType">
                  <el-option
                    v-for="item in SSoptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="相对方名称"
              prop="budgetname"
              width="400px"
            >
              <template slot-scope="scope">
                <div style="margin-left: 40px; display: flex">
                  <el-input
                    disabled
                    v-model="scope.row.budgetname"
                    style="width: 60%; margin-left: 40px"
                  />
                  <el-button
                    :style="{ marginLeft: '10px' }"
                    type="primary"
                    @click="
                      $refs.xdfINfo.show('HTGL001', 'budgetname', scope.$index)
                    "
                  >
                    选择
                  </el-button>
                </div>
              </template>
            </el-table-column>

            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="text" @click="handleDelete(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col>
          <el-divider>关联OA公文/协同列表</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openOA()">新增</el-button>
          </div>
          <el-table :data="tableDataOA">
            <el-table-column align="center" label="标题" prop="subject" />
            <el-table-column align="center" label="类别" prop="cateGoryName">
              <template slot-scope="scope">
                <span v-if="scope.row.cateGoryName == 'edoc'">公文</span>
                <span v-if="scope.row.cateGoryName == 'col'">协同</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="类型" prop="stateName">
              <template slot-scope="scope">
                <span v-if="scope.row.stateName == '19'">发文</span>
                <span v-if="scope.row.stateName == '20'">收文</span>
                <span v-if="scope.row.stateName == '21'">签报</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="发送人" prop="sendName" />
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="text" @click="handleDetail(scope.row)">
                  详细
                </el-button>
                <el-button type="text" @click="handleOADelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>上传审批附件</el-divider>
          <AttachList
            :att-list="formData.attList"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
            :att-type="2"
          />
        </el-col> -->
        <el-col
          v-if="showPlanList && formData.contractplan === '是'"
          :span="24"
        >
          <el-divider>合同阶段信息</el-divider>
          <JieduanList
            :contract="formData"
            @data-change="formData.nodeList = $event"
            ref="jieduanListRef"
            :XDFList="this.JDDetailXDFInfo"
          />
        </el-col>
        <el-col :span="24">
          <el-divider>合同正文</el-divider>
        </el-col>
        <el-col :span="4">
          <el-form-item label-width="0" prop="describe">
            <!-- <UEditor
              v-if="true || (showEditor && !beenClose)"
              ref="ueditor"
              v-model="formData.describe"
              :height="300"
              :templates="templates"
            /> -->
            <el-button
              class="add-btn"
              size="mini"
              type="primary"
              @click="openOffice()"
            >
              编辑合同正文
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-upload
            ref="upload"
            accept=".doc,.docx"
            :show-file-list="false"
            :action="uploadUrl"
            :on-success="handleSuccess1"
            :before-upload="handleFileBefore"
            name="uploadedFile"
          >
            <el-button class="add-btn" size="mini" type="primary">
              上传本地文件
            </el-button>
          </el-upload>
        </el-col>
        <el-col :span="3">
          <el-button
            :disabled="!contractid"
            class="add-btn"
            size="mini"
            type="primary"
            @click="openTemplate()"
          >
            选择合同范本
          </el-button>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api + '?contractId=' + contractid"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="pdfList"
            :before-upload="handleBeforeUpload"
            style="text-align: right"
          >
            <el-button type="primary" :disabled="!contractid">上传</el-button>
          </el-upload>
        </el-col>
        <el-col :span="24">
          <el-table :data="pdfList">
            <el-table-column
              align="center"
              label="序号"
              type="index"
              width="50"
            ></el-table-column>
            <el-table-column
              align="center"
              label="合同文件名称"
              prop="contentPdfName"
            />
            <el-table-column
              align="center"
              label="上传人"
              width="70"
              prop="realname"
            />
            <!-- <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="contentPdfSize"
            />-->
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handleDownFilePDf(row)"
                  size="mini"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFilePDf(row)"
                  size="mini"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteFilePDF(row, $index)"
                  size="mini"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <!-- <el-col :span="24">
          <el-divider>审核合同文本</el-divider>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api2 + '?contractId=' + contractid"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess2"
            :file-list="pdfList2"
            :before-upload="handleBeforeUpload"
            style="text-align: right"
          >
            <el-button type="primary" :disabled="!contractid">上传</el-button>
          </el-upload>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-table :data="pdfList2">
            <el-table-column
              align="center"
              label="序号"
              type="index"
              width="50"
            />
            <el-table-column
              align="center"
              label="合同文件名称"
              prop="fileName"
            />
            <el-table-column
              align="center"
              label="上传人"
              prop="uploaderName"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDownFilePDf2(row)"
                  size="mini"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFilePDf2(row)"
                  size="mini"
                >
                  预览
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save" :disabled="changeSaveBtn">
        确 定
      </el-button>

      <el-button
        type="primary"
        @click="ymsubmit"
        :disabled="changeSaveBtn"
        v-if="isWfqdedit || fromId == -1"
      >
        提 交
      </el-button>
    </div>
    <xdfInfo ref="xdfINfo" @selected="handleXdfInfoSelected" />
    <xdf-options ref="xdf" @selected="handleXdfSelected" />
    <bank-options ref="bank" @selected="handleBankSelected" />
    <setup-options ref="setup" @selected="handleSetupSelected" />
    <unit-options ref="unit" @selected="handleUnitSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <SealDepartment ref="department" @selected="handleDepartmentSelected" />
    <ChooseType
      v-if="chooseStatus"
      @close="closeChooseType"
      ref="type"
      @selected="handleTypeSelected"
    />
    <TextEditor ref="webOffice" />
    <oaList ref="oaList" @selected="handleOA" />
    <TemplateTable ref="templateTable" @selected="handleTemplateSelected" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { debounce } from '@/utils'
  import {
    deleteAttach,
    generateNo,
    getContractItem,
    getContractTemplates,
    getContractTypes,
    saveContract,
    contractPdfList,
    getPrivewAttInfo,
    downContractPdf,
    deleteContractPdf,
    getCyhwUnitOAList,
    remoceOaDocument,
    getOaurl,
    contractExamList,
    deleteContractExam,
    downContractExam,
  } from '@/api/contract/manage'
  import { formatDate } from '@/utils/index'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import UEditor from '@/components/UEditor'
  import { number2text } from '@/utils'
  import { contractStatusOptions } from '@/views/contract/consts'
  import AttachList from '../AttachList.vue'
  import JieduanList from '../block/JieduanList.vue'
  import SubList from '../block/SubList.vue'
  import ChooseType from '../ChooseType.vue'
  import BankOptions from '../options/bank.vue'
  import DepartmentOptions from '../options/department.vue'
  // import ExecutorOptions from '../options/executor.vue'
  import ExecutorOptions from '@/components/CompanySelectUserByTree'
  import SealDepartment from '../options/sealDepartment.vue'
  import SetupOptions from '../options/setupInfo.vue'
  import UnitOptions from '../options/unit.vue'
  import XdfOptions from '../options/newXdf.vue'
  import xdfInfo from '../options/xdf.vue'
  import oaList from '../options/oaList.vue'
  import { comboFields } from './methods'
  import TextEditor from './TextEditor.vue'
  import TemplateTable from './TemplateTable.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  const { baseURL } = require('@/config')
  import store from '@/store'
  export default {
    name: 'CreateEdit',
    components: {
      UEditor,
      XdfOptions,
      BankOptions,
      SetupOptions,
      UnitOptions,
      ExecutorOptions,
      DepartmentOptions,
      SealDepartment,
      SubList,
      JieduanList,
      AttachList,
      ChooseType,
      TextEditor,
      Resubmit,
      oaList,
      CandidateUserSelect,
      TemplateTable,
      xdfInfo,
    },
    props: {
      form: {
        type: Object,
        default: () => {},
      },
      contracttype: {
        type: String,
        default: '',
      },
      isWfqdedit: {
        type: Boolean,
        default: false,
      },
    },
    provide() {
      return {
        fatherFetchItem: this.fetchItem,
      }
    },
    data() {
      return {
        buttonLoading: false,
        baseApi: baseURL,
        api: '/contract/importContractPdf',
        api2: '/contract/importContractExam',
        headers: {
          token: store.getters['user/token'],
        },
        tableDataProject: [],
        tableDataOA: [],
        loading: false,
        visible: false,
        changeSaveBtn: false,
        //刷新组件状态，否则组件没用数据
        Jie: true,
        showEditor: false,
        beenClose: false,
        templates: [],
        currentEdit: 'moren',
        showSubFields: false,
        subTitle: '',
        subFields: [
          { value: 'infoname', label: '货物名称' },
          { value: 'infodesc', label: '运输说明' },
          { value: 'infoxh', label: '规格型号' },
          { value: 'infoprice', label: '货物价值' },
        ],
        JDDetailXDFInfo: {},
        formFields: {
          changetype: undefined,
        },
        localList: [], // 本地缓存新增的附件列表
        uploadUrl: '',
        formData: {
          flowId: 622316,
          flowid: 622316,
          flowname: 'HTGL002',
          recordtype: 'HTGL002',
          contractid: undefined,
          contractno: '',
          contractname: undefined,
          contractitem: undefined,
          contracttype: null,
          startdate: undefined,
          enddate: undefined,
          counterparttype: undefined,
          contractdatetype: undefined,
          contractxz: undefined,
          contractbd: undefined,
          entrustStaffName: undefined,
          contractzd: undefined,
          zxunit: undefined, // 执行单位key
          orgname: undefined, // 执行单位value
          contractdept: undefined, // 执行部门key
          orgmeno: undefined, // 执行部门value
          contractstaff: undefined, // 执行人key
          realname: undefined, // 执行人value
          contractxdfxinfo: undefined, //相对方主键key
          budgetname: undefined, // 相对方value
          topicname: undefined, // 立项信息key（是name）
          topicid: undefined, // 立项信息value
          counterpartbank: undefined, // 银行key
          bankaccount: undefined, // 银行value
          bankkhyh: undefined, // 开户银行
          momoconcat: undefined,
          dctype: undefined,
          contractmoney: undefined,
          moneytype: undefined,
          hzsumowing: undefined,
          describe: undefined,
          account: undefined,
          attList: [],
          nodeList: [],
          informationList: [],
          signingList: [],
          isbigmatter: '是',
          matterorg: undefined,
          contractstatus: 0,
          typefl: undefined,
          ismany: '否',
          agreementcount: 0,
        },
        rules: {
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          contractitem: [
            {
              required: false,
              message: '请输入项目编号',
              trigger: 'blur',
            },
          ],
          contracttype: [
            {
              required: true,
              message: '请输入合同类型',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请选择承办人',
              trigger: 'change',
            },
          ],
          orgmeno: [
            {
              required: true,
              message: '请选择承办部门',
              trigger: 'change',
            },
          ],
          contractzd: [
            {
              required: true,
              message: '请选择合同层级',
              trigger: 'change',
            },
          ],
          orgname: [
            {
              required: true,
              message: '请选择我方签署主体',
              trigger: 'change',
            },
          ],
          budgetname: [
            {
              required: true,
              message: '请选择相对方信息',
              trigger: 'change',
            },
          ],
          dctype: [
            {
              required: true,
              message: '请选择收付款方向',
              trigger: 'blur',
            },
          ],
          contractmoney: [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ],
        },
        chooseStatus: false,
        oaurl: '',
        ticket: '',
        title: '',
        dialogFormVisible: false,
        options: [],
        typeOptions: [],
        xdfOptions: [],
        changeOptions: [
          {
            label: '合同名称变更',
            value: '合同名称变更',
          },
          {
            label: '合同项目变更',
            value: '合同项目变更',
          },
          {
            label: '合同期限变更',
            value: '合同期限变更',
          },
          {
            label: '相对方变更',
            value: '相对方变更',
          },
          {
            label: '执行信息变更',
            value: '执行信息变更',
          },
          {
            label: '合同金额变更',
            value: '合同金额变更',
          },
          {
            label: '合同内容变更',
            value: '合同内容变更',
          },
          {
            label: '全部信息变更',
            value: '全部信息变更',
          },
        ],
        contractLevelOptions: [
          { label: '一般', value: '1' },
          { label: '重要', value: '2' },
          { label: '重大', value: '3' },
        ],
        SSoptions: [
          {
            value: '甲',
            label: '甲',
          },
          {
            value: '乙',
            label: '乙',
          },
          {
            value: '丙',
            label: '丙',
          },
          {
            value: '丁',
            label: '丁',
          },
          {
            value: '戊',
            label: '戊',
          },
          {
            value: '己',
            label: '己',
          },
          {
            value: '庚',
            label: '庚',
          },

          {
            value: '辛',
            label: '辛',
          },
          {
            value: '壬',
            label: '壬',
          },
          {
            value: '癸',
            label: '癸',
          },
        ],
        pdfList: [],
        pdfList2: [],
        disabled: false,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {
      showPlanList() {
        if (this.currentEdit !== 'moren') {
          if (this.currentEdit == 'zhanlue') {
            return false
          }
          return true
        } else {
          if (this.formData.contractplan) {
            return true
          }
          return false
        }
      },
    },

    watch: {
      'formData.nodeList'(val, val2) {
        if (val != val2) {
          // 移除组件
          this.Jie = false
          // 在组件移除后，重新渲染组件
          // this.$nextTick可实现在DOM 状态更新后，执行传入的方法。
          this.$nextTick(() => {
            this.Jie = true
            this.$refs['jieduanListRef'].fetchData()
          })
        }
      },
      'formData.matterorg': {
        handler(val) {
          if (val && val == '董事会') this.formData.isbigmatter = '是'
        },
      },
      'formData.contractchildren'(val) {
        if (val == '是') {
          this.resetXdf()
        }
      },
      'formData.describe'(val) {
        if (!this.formData.contractname) {
          this.formData.describe = val.replace('$[contract.contractname]', '')
        } else if (!this.formData.contractno) {
          this.formData.describe = val.replace('$[contract.contractno]', '')
        } else if (!this.formData.contractmoney) {
          this.formData.describe = val.replace('$[contract.contractamount]', '')
        } else if (!this.formData.contractitem) {
          this.formData.describe = val.replace('$[contract.contractItem]', '')
        } else if (!this.formData.realname) {
          this.formData.describe = val.replace('$[contract.executor]', '')
        } else if (!this.formData.hzsumowing) {
          this.formData.describe = val.replace('$[contract.rmbinwords]', '')
        } else if (!this.formData.counterpartcode) {
          this.formData.describe = val.replace('$[contract.coupersion]', '')
        } else if (!this.formData.contractbd) {
          this.formData.describe = val.replace('$[contract.personincharge]', '')
        } else if (!this.formData.bankkhyh) {
          this.formData.describe = val.replace(
            '$[contract.counterpartHank]',
            ''
          )
        } else if (!this.formData.counumber) {
          this.formData.describe = val.replace('$[contract.counumber]', '')
        } else if (!this.formData.budgetname) {
          this.formData.describe = val.replace('$[contract.couname]', '')
        } else if (!this.formData.counterpartaddress) {
          this.formData.describe = val.replace('$[contract.couaddress]', '')
        } else if (!this.formData.contactsphone) {
          this.formData.describe = val.replace('$[contract.contactsPhone]', '')
        } else if (!this.formData.bankaccount) {
          this.formData.describe = val.replace(
            '$[contract.counterpartHankAccount]',
            ''
          )
        } else if (!this.formData.contacts) {
          this.formData.describe = val.replace(
            '$[contract.counterpartHankAccount]',
            ''
          )
        } else if (!this.formData.contractzd) {
          this.formData.describe = val.replace(
            '$[contract.counterpartHankAccount]',
            ''
          )
        }

        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]

          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.describe = s
        }
      },
    },
    mounted() {
      this.changeSaveBtn = false
    },
    created() {
      this.$bus.$off('changeSaveBtn').$on('changeSaveBtn', () => {
        this.changeSaveBtn = true
      })
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      dctypeChange(val) {
        if (val == '无') {
          this.formData.contractmoney = 0
          this.formData.hzsumowing = '零元整'
        }
      },
      jijiatypeChange(val) {
        if (val == '无金额') {
          this.formData.contractmoney = 0
        }
        if (val == '非固定总价') {
          this.rules.contractmoney = [
            {
              required: false,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.contractmoney = [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        }
      },
      handleTemplateSelected(val) {
        this.$store.commit('acl/contractidd', val.contractid)
        this.$store.commit('acl/contractidd2', this.contractid)
        // this.$refs['webOffice'].show()
        const info = JSON.parse(localStorage.getItem('userInfo'))
        let temp = document.createElement('form')
        temp.action = `https://office.wenxin.example.com/api/office/editRealContract?fileType=word&templateId=${val.contractid}&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        temp.method = 'post'
        temp.target = '_blank'
        document.body.appendChild(temp)
        temp.submit()
      },
      openTemplate() {
        this.$refs['templateTable'].show(this.formData.contracttype)
      },
      //office
      openWebOffice() {
        this.$store.commit('acl/contractidd', this.contractid)
        this.$refs['webOffice'].show()
      },
      //office
      openOffice() {
        this.$store.commit('acl/contractidd', this.contractid)
        const info = JSON.parse(localStorage.getItem('userInfo'))
        let temp = document.createElement('form')
        temp.action = `https://office.wenxin.example.com/api/office/editRealContract?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        temp.method = 'post'
        temp.target = '_blank'
        document.body.appendChild(temp)
        temp.submit()
      },
      executorShow(e) {
        if (e) {
          this.executorType = e
        } else {
          this.executorType = ''
        }
        this.$refs['executor'].show()
      },
      //回调
      handleTypeSelected(item) {
        this.chooseStatus = true
        this.$nextTick(() => {
          this.$refs['common'].showEdit(undefined, item.typename)
        })
      },
      //关闭选择合同类型
      closeChooseType() {
        this.chooseStatus = false
      },

      async getNo() {
        const res = await generateNo({
          flowId: this.formData.flowId,
        })
        this.formData.contractno = res.data.contractno
      },
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.map((item) => {
          return {
            label: item.typename,
            value: item.typeid,
          }
        })
      },
      async handleOADelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await remoceOaDocument({
            documentId: row.documentId,
          })
          if (res.code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.getOAList(this.contractid)
          }
        })
      },
      openOA() {
        if (!this.contractid) {
          this.$baseMessage('请先保存合同', 'error', 'vab-hey-message-success')
          return
        }
        this.$refs.oaList.show(this.contractid)
      },
      handleOA() {
        this.getOAList(this.contractid)
      },
      async getOAList(contractid) {
        const oaList = await getCyhwUnitOAList({
          contractId: contractid,
        })
        this.tableDataOA = oaList.data
      },
      async getOATicket() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        //
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        if (this.$store.state.work.processMobile) {
          window.open(`${this.oaurl}${row.h5url}&ticket=${this.ticket}`)
        } else {
          window.open(`${this.oaurl}${row.url}&ticket=${this.ticket}`)
        }
      },
      async fetchItem(row) {
        this.contractid = row.contractid
        const fileres = await contractPdfList({
          contractId: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList = fileres.data.tlist || []

        const fileres2 = await contractExamList({
          id: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList2 = fileres2.data.tlist || []
        const res = await getContractItem({
          contractId: row.contractid,
          flowId: this.formData.flowId,
          flowname: row.recordtype,
        })
        // this.getOAList(row.contractid)
        // this.getOATicket()
        this.JDDetailXDFInfo = res //保存合同阶段相对方列表需要的信息
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.tcu[key]
        })

        if (this.formData.jijiatype == '非固定总价') {
          this.rules.contractmoney = [
            {
              required: false,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.contractmoney = [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        }
        this.formData.entrustStaffName = res.data.tcu.entrustStaffName
        this.formData.flowId = res.data.flowid
        this.formData.attList = res.data.attList
        this.formData.nodeList = res.data.nodeList
        this.formData.informationList = res.data.informationList
        this.formData.signingList = res.data.signingList
        this.formData.contractstatus = res.data.tcu.contractstatus
        this.formData.typefl = res.data.tcu.typefl
        this.formData.ismany = res.data.tcu.ismany
        this.formData.agreementcount = res.data.tcu.agreementcount
        const { bankinfo } = res.data.tcu
        if (bankinfo) {
          this.formData.counterpartbank = bankinfo.bankid
          this.formData.bankaccount = bankinfo.bankaccount
          this.formData.bankkhyh = bankinfo.bankkhyh
        }

        const info =
          res.data.tcu.budgetList &&
          res.data.tcu.budgetList.map((res) => {
            return {
              budgetname: res.budgetname,
              bugetId: res.budgetid,
              bugetType: res.budgettype,
            }
          })
        this.tableDataProject = info || []
        // 重置localList
        this.localList = []
      },
      // showDetail(row, type) {
      //   const {
      //     formData,
      //     formFields,
      //     subFields,
      //     showSubFields,
      //     subTitle,
      //     currentEdit,
      //   } = comboFields(type, false)
      //   this.formData = formData
      //   this.formFields = formFields
      //   this.currentEdit = currentEdit
      //   this.subFields = subFields
      //   this.showSubFields = showSubFields
      //   this.subTitle = subTitle

      //   this.title = '查看'
      //   this.disabled = true
      //   this.fetchItem(row)
      //   this.dialogFormVisible = true
      // },
      mapContractStatus(row) {
        const res = contractStatusOptions.filter((item) => {
          return item.value === row.contractstatus
        })
        return res[0].label
      },
      async showEdit(row, type, fromId, flowtaskinfoflowid, ymFromId, status) {
        if (fromId) {
          this.fromId = fromId + ''
          this.fromIdcopy = fromId + '' // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid + ''
        }
        if (ymFromId) {
          this.ymFromId = ymFromId + ''
        }
        this.status = status + ''

        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, false)
        this.formData = formData
        this.formFields = formFields
        this.currentEdit = currentEdit
        this.subFields = subFields
        this.showSubFields = showSubFields
        this.subTitle = subTitle
        if (!row || !row.contractno || fromId == -1) {
          this.title = '添加'
          this.formData.contractzd = ''
          this.formData.typefl = typename
          this.formData.contracttype = type
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.realname = userInfo.realname
          this.formData.contractstaff = userInfo.staffid
          this.formData.orgmeno = userInfo.linkDetp.orgname
          this.formData.jbdept = userInfo.linkDetp.orgid
          this.getNo()
        } else {
          this.title = '编辑'
          this.fetchItem(row)
        }
        this.dialogFormVisible = true
        this.beenClose = false
        this.showEditor = false
        this.renderTemplates(type)
      },
      async renderTemplates(type) {
        // 获取范本模板列表
        const data = await getContractTemplates({
          contractType: type,
        })

        if (data && data.length) {
          this.templates = data.map((item) => {
            return {
              pre: 'pre1.png',
              title: '',
              preHtml: item.contractname,
              html: item.describe,
            }
          })
        }
        this.showEditor = true
      },
      //回调
      resetXdf() {
        this.formData.recordparent = undefined
        this.formData.parentname = undefined
      },
      //回调
      handleXdfSelected(val, field) {
        if (field == 'budgetname') {
          this.formData.contractxdfxinfo = val.budgetid
          this.formData.budgetname = val.budgetname
          this.formData.counterparttype = val.counterparttype
          this.formData.director = val.director
          this.formData.counterpartaddress = val.counterpartaddress
          this.formData.contacts = val.contacts
          this.formData.contactsphone = val.contactsphone
          this.formData.counterpartno = val.counterpartno
        } else {
          this.formData.recordparent = val.contractid
          this.formData.parentname = val.contractname
        }
      },
      handleChoseBankClick() {
        if (!this.formData.contractxdfxinfo) {
          const label = this.formFields['budgetname'].label
          this.$baseMessage(
            `请先选择${label}！`,
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$refs.bank.show({
          budgetId: this.formData.contractxdfxinfo,
        })
      },
      //回调
      handleBankSelected(node) {
        this.formData.counterpartbank = node.bankid
        this.formData.bankaccount = node.bankaccount
        this.formData.bankkhyh = node.bankkhyh
      },
      //回调
      handleSetupSelected(node) {
        this.formData.topicname = node.projectname
        this.formData.topicid = node.projectid
      },
      //回调
      handleUnitSelected(node) {
        this.formData.zxunit = node.id
        this.formData.orgname = node.label
        this.$forceUpdate()
      },
      //回调
      handleExecutorSelected(node) {
        console.log('node', node)
        console.log('executorType', this.executorType)
        if (this.executorType !== '') {
          this.$set(this.formData, this.executorType + 'Id', node.staffid)
          this.$set(this.formData, this.executorType + 'Name', node.realname)
        } else {
          this.formData.realname = node.realname
          this.formData.contractstaff = node.staffid
        }
        this.$forceUpdate()
      },
      //回调
      handleDepartmentSelected(node) {
        this.formData.contractdept = node.id
        this.formData.orgmeno = node.name
      },
      handleBlur() {
        this.formData.hzsumowing = number2text(this.formData.contractmoney)
        this.money = this.formData.contractmoney
      },
      handleUploadSuccess(val) {
        this.localList.push(val.data)
      },
      async handleDeleteAtt(row, index) {
        const { code } = await deleteAttach({
          attid: row.attid,
        })
        if (code == 1) {
          if (row.type == 'local') {
            this.localList.splice(index - this.formData.attList.length, 1)
          } else {
            this.formData.attList.splice(index, 1)
          }
        }
      },
      handleDeleteSub(index) {
        this.formData.informationList.splice(index, 1)
      },
      async handleDownFilePDf(list) {
        const data = await downContractPdf({
          contentPdfId: list.contentPdfId,
        })
        let filename = list.contentPdfName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          // type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },

      async handleDownFilePDf2(list) {
        const data = await downContractExam({
          id: list.id,
        })
        let filename = list.fileName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          // type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      handleBeforeUpload(file) {
        // if (this.pdfList.length > 0) {
        //   this.$baseMessage('只能上传一个文件,请先删除！', 'error')
        //   return false
        // }
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.code == '1') {
          this.pdfList.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleSuccess2(file) {
        if (file.code == '1') {
          this.pdfList2.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      async handlePreviewFilePDf(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.contentPdfId,
          attType: 4,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      async handlePreviewFilePDf2(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.id,
          attType: 5,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      async handleDeleteFilePDF(list, index) {
        await deleteContractPdf({ contentPdfId: list.contentPdfId })
        this.pdfList.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      async handleDeleteFilePDF2(list, index) {
        await deleteContractExam({ id: list.id })
        this.pdfList2.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.tableDataProject = []
        this.buttonLoading = false
        this.$bus.$emit('updateMsg', 0)
        this.$emit('fetch-data')
        this.dialogFormVisible = false
        this.beenClose = true
      },
      save(text) {
        this.loading = true
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const arrAttid = this.localList.map((item) => item.attid)
            if (arrAttid && arrAttid.length) {
              this.formData.attids = arrAttid.join(',')
            }
            this.$delete(this.formData, 'flowid')
            const { describe, budgetList, ...other } = this.formData
            const { msg, data } = await saveContract({
              ...other,
              contractxdf: JSON.stringify(this.tableDataProject),
            })
            // this.formData = data
            this.loading = false
            if (!text || typeof text !== 'string') {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }

            // this.$emit('fetch-data')
            // this.fetchItem(data)
          }
        })
      },
      // 添加点击按钮
      handleAdds() {
        // if (!this.contractid) {
        //   this.$baseMessage(
        //     '请先到页面底部保存合同基本信息后，再新增相对方信息',
        //     'error',
        //     'vab-hey-message-error'
        //   )
        //   return
        // }
        this.tableDataProject.push({
          budgetname: '',
          bugetType: '',
        })
      },
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            this.tableDataProject.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      //回调
      handleXdfInfoSelected(info, b, c) {
        this.tableDataProject[c].budgetname = info.budgetname
        this.tableDataProject[c].bugetId = info.budgetid
      },
      //流程提交
      ymsubmit: debounce(function (val) {
        this.handleApproval(val)
      }, 1000), //延迟执行时间可以视具体情况而定
      async handleApproval() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.save('提交')
            // this.buttonLoading = true
            this.$refs.resubmit.ymsubmit(this.formData.contracttype)
          }
        })
      },
      handleSuccess1() {
        this.openOffice()
      },
      handleFileBefore(file) {
        return new Promise((resolve, reject) => {
          const info = JSON.parse(localStorage.getItem('userInfo'))
          // 此处动态配置action URL
          this.uploadUrl = `https://office.wenxin.example.com/api/office/editRealContract?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}&upload=true`
          // dom上传地址更新完成后，触发上传
          this.$nextTick(() => resolve())
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
  .el-form .el-form-item .el-button {
    position: absolute;
  }
</style>
