<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form
      v-if="show == 0"
      ref="form"
      label-width="140px"
      :model="form"
      :rules="rules"
      v-loading="loading"
    >
      <el-row :gutter="15">
        <el-col :span="12">
          <el-form-item label="纠纷编号" prop="disputeno">
            <el-input
              v-model.trim="form.disputeno"
              clearable
              :disabled="true"
              placeholder="请输入纠纷编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷名称" prop="disputeitem">
            <el-input
              v-model.trim="form.disputeitem"
              clearable
              :disabled="isDetail"
              placeholder="请输入纠纷名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷类型" prop="disputetype">
            <el-select
              v-model="form.disputetype"
              :style="{ width: '100%' }"
              :disabled="isDetail"
            >
              <el-option value="一般纠纷">一般纠纷</el-option>
              <el-option value="重大纠纷">重大纠纷</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否关联合同" prop="glht">
            <el-radio-group
              v-model.trim="form.glht"
              :style="{ height: '45px', lineHeight: '45px' }"
            >
              <el-radio :label="1" :disabled="isDetail">是</el-radio>
              <el-radio :label="2" :disabled="isDetail">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷阶段" prop="disputetype">
            <el-select
              v-model="form.disputetypse"
              :style="{ width: '100%' }"
              :disabled="isDetail"
            >
              <el-option value="登记">登记</el-option>
              <el-option value="协商">协商</el-option>
              <el-option value="诉讼">诉讼</el-option>
              <el-option value="仲裁">仲裁</el-option>
              <el-option value="结案">结案</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉诉金额（万元）" prop="litigationamount">
            <el-input
              v-model.trim="form.litigationamount"
              clearable
              type="number"
              @input="$forceUpdate()"
              :disabled="isDetail"
              placeholder="请输入涉诉金额（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.glht === 1">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model.trim="form.contractname"
              clearable
              placeholder="请选择合同名称"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.xzht.show()"
              :disabled="isDetail"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.glht === 1">
          <el-form-item label="合同编号">
            <el-input
              v-model.trim="form.contractno"
              clearable
              placeholder="请输入合同编号"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.glht === 1">
          <el-form-item label="合同执行人">
            <el-input
              v-model.trim="form.realname"
              clearable
              placeholder="请输入合同执行人"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原告" prop="plaintiff">
            <el-input
              v-model.trim="form.plaintiff"
              clearable
              placeholder="请输入原告"
              :disabled="isDetail"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被告" prop="defendant">
            <el-input
              v-model.trim="form.defendant"
              clearable
              placeholder="请输入被告"
              :disabled="isDetail"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉诉标的" prop="disputecours">
            <el-input
              v-model.trim="form.disputecours"
              clearable
              placeholder="请输入涉诉标的"
              :style="{ width: '100%' }"
              :disabled="isDetail"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公司经办人" prop="zxstaffname">
            <el-input
              v-model.trim="form.zxstaffname"
              clearable
              placeholder="请选择纠纷承办人"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.executor.showEdit()"
              :disabled="isDetail"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否紧急事项" prop="isuegent">
            <el-radio-group
              v-model.trim="form.isuegent"
              :style="{ height: '45px', lineHeight: '45px' }"
            >
              <el-radio :label="1" :disabled="isDetail">是</el-radio>
              <el-radio :label="2" :disabled="isDetail">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="诉讼地位" prop="whethersued">
            <el-select
              v-model="form.whethersued"
              :style="{ width: '100%' }"
              :disabled="isDetail"
            >
              <el-option label="原告" :value="1">原告</el-option>
              <el-option label="被告" :value="2">被告</el-option>
              <!-- <el-option label="上诉人" :value="3">上诉人</el-option>
              <el-option label="被上诉人" :value="4">被上诉人</el-option> -->
              <!-- <el-option label="第三人" :value="5">第三人</el-option> -->
              <!-- <el-option label="再审申请认" :value="6">再审申请人</el-option>
              <el-option label="被申请人" :value="7">被申请人</el-option> -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计办结时间" prop="enddate1">
            <el-date-picker
              v-model.trim="form.enddate1"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入预计办结时间"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              :disabled="isDetail"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-col :span="12">
          <el-form-item label="是否外聘律师" prop="isattorney">
            <el-radio-group v-model.trim="form.isattorney">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col> -->

      <el-row :gutter="15">
        <el-col :span="24">
          <el-form-item label="初步解决建议" prop="solutionsuggestions">
            <el-input
              v-model="form.solutionsuggestions"
              placeholder="请输入初步解决建议"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="isDetail"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.isuegent === 1">
          <el-form-item label="紧急事项情况说明" prop="urgentmemo">
            <el-input
              v-model="form.urgentmemo"
              placeholder="请输入紧急事项情况说明"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="isDetail"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="法务部审核意见" prop="legalexam">
            <el-input
              v-model="form.legalexam"
              disabled
              placeholder="请输入法务部审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总法律顾问审核意见" prop="counselexam">
            <el-input
              v-model="form.counselexam"
              disabled
              placeholder="请输入总法律顾问审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="董事长审核意见" prop="chairmanexam">
            <el-input
              disabled
              v-model="form.chairmanexam"
              placeholder="请输入董事长审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总经理审核意见" prop="gmanexam">
            <el-input
              v-model="form.gmanexam"
              disabled
              placeholder="请输入总经理审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>我方代理人</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button @click="addLine" type="success" v-if="!isDetail">
              新增
            </el-button>
          </div>
          <el-table
            :data="dllsData"
            style="width: 100%; margin: 10px 0 50px 0%"
          >
            <el-table-column align="center" prop="isattorney" label="是否外聘">
              <template #default="{ row }">
                {{ row.isattorney === 1 ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="attorney" label="代理人" />
            <el-table-column
              align="center"
              prop="attorneyphont"
              label="联系方式"
            />
            <el-table-column
              align="center"
              prop="attorneyphont"
              label="操作"
              v-if="!isDetail"
            >
              <template #default="{ row }">
                <el-button @click="deleteRow(row.id)" type="text" size="small">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!isDetail">
            <el-button v-if="!form.disputeid" type="success" @click="hold()">
              上传
            </el-button>
            <el-upload
              v-else
              ref="upload"
              :accept="accept"
              :show-file-list="false"
              :action="baseApi + api"
              :before-upload="handleBeforeUpload"
              :data="uploadData"
              :file-list="fileList"
              :headers="headers"
              :limit="1"
              :on-error="onError"
              :on-remove="handleRemove"
              :on-success="onSuccess"
              :on-exceed="onExceed"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="uploadlist">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
              v-if="!isDetail"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="downloadData(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>
    <el-row v-if="show == 1" :gutter="15" v-loading="loading">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="登记编号" prop="disputeno">
            <span>{{ form.disputeno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷名称" prop="disputeitem">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷类型" prop="disputetype">
            <span>{{ form.disputetype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉诉金额（万元）" prop="litigationamount">
            <span>{{ form.litigationamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="合同名称"
            prop="contractname"
            v-if="form.glht == 1"
          >
            <span>{{ form.contractname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" v-if="form.glht == 1">
            <span>{{ form.contractno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同执行人" v-if="form.glht == 1">
            <span>{{ form.realname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原告" prop="plaintiff">
            <span>{{ form.plaintiff }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被告" prop="defendant">
            <span>{{ form.defendant }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉诉标的" prop="disputecours">
            <span>{{ form.disputecours }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公司经办人" prop="zxstaffname">
            <span>{{ form.zxstaffname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否紧急事项" prop="isuegent">
            <span>{{ form.isuegent ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼地位" prop="whethersued">
            <span>
              {{
                form.whethersued == 1
                  ? '原告'
                  : form.whethersued == 2
                  ? '被告'
                  : form.whethersued == 5
                  ? '第三人'
                  : ''
              }}
            </span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最晚办结时间" prop="enddate1">
            <span>{{ form.enddate1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外聘律师" prop="isattorney">
            <span>{{ form.isattorney ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理律师" prop="attorney">
            <span>{{ form.attorney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理律师联系电话" prop="attorneyphont">
            <span>{{ form.attorneyphont }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="初步解决建议" prop="solutionsuggestions">
            <span>{{ form.solutionsuggestions }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.isuegent === 1">
          <el-form-item label="紧急事项情况说明" prop="urgentmemo">
            <span>{{ form.urgentmemo }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="法务部审核意见" prop="legalexam">
            <span>{{ form.legalexam }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总法律顾问审核意见" prop="counselexam">
            <span>{{ form.counselexam }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="董事长审核意见" prop="chairmanexam">
            <span>{{ form.chairmanexam }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总经理审核意见" prop="gmanexam">
            <span>{{ form.gmanexam }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>我方代理人</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <!-- <el-button @click="addLine" type="success">新增</el-button> -->
          </div>
          <el-table
            :data="dllsData"
            style="width: 100%; margin: 10px 0 50px 0%"
          >
            <el-table-column align="center" prop="isattorney" label="是否外聘">
              <template #default="{ row }">
                {{ row.isattorney === 1 ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="attorney" label="代理人" />
            <el-table-column
              align="center"
              prop="attorneyphont"
              label="联系方式"
            />
            <el-table-column align="center" prop="attorneyphont" label="操作">
              <template>
                <!-- <el-button @click="deleteRow(row.id)" type="text" size="small">
                  删除
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="uploadlist">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="downloadData(row)">
                  下载
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
      <el-button
        v-if="
          (form.disputestatus == 2 || form.disputestatus == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
    <xzht-options ref="xzht" @selecteded="handleSsjd" />
    <!-- <executor-options ref="executor" @selected="handleSelected" /> -->
    <project-manage
      @projectManage="getChildlistPro"
      ref="executor"
    ></project-manage>
    <!-- 代理律师新增编辑 -->
    <lower-edit ref="lowerEdit" @fetch-lawer-list="fetchLawerList" />
    <!-- 提交 -->
    <el-dialog
      @close="currentClose"
      title="选择分支"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        label-width="100px"
        ref="fzforms"
        :modal="fzforms"
        :rules="fzRules"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzforms.branchStrs"
            @change="handlefzChange"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :label="item.nodeName"
              :value="item.nodeId"
              :key="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="currentClose">取消</el-button>
        <el-button type="primary" @click="save4">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      @close="close1"
      title="选择分支"
      :visible="visible1"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="visible1"
    >
      <el-form
        :modal="fzform1"
        label-width="100px"
        ref="fzform1"
        :rules="fzRules1"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzform1.branchStrs"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.nodeId"
              :label="item.nodeName"
              :value="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="close1">取消</el-button>
        <el-button type="primary" @click="save1">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="选择候选人"
      :visible.sync="visible2"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close2"
      v-if="visible2"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item label="候选人" prop="transferStaffName">
          <!-- <el-input
            disabled
            placeholder="请选择候选人"
            v-model="formData2.transferStaffName"
            style="width: 79%; margin-right: 8px"
          ></el-input>
          <el-button type="primary" @click="handleSelect">请选择</el-button> -->
          <CandidateUserSelect
            :clearType="clearType"
            @selected="handleCandSelect1"
            :index="0"
            :nodeId="candidateData.nodeId"
            :candidateData="candidateData"
            multiple
            placeholder="请选择候选人"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close2">取 消</el-button>
        <el-button type="primary" @click="save2">确 定</el-button>
      </span>
    </el-dialog>
  </el-dialog>
</template>

<script>
  import {
    caseInformationModify,
    caseInformationSave,
    deleAttacheMent,
    disputeRegisterDetail,
    findAttacheMent,
    legalAttorney,
    legalAttorneyDelete,
    getDisputeNo,
  } from '@/api/fwgl/legal'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import { baseURL } from '@/config/net.config'
  import store from '@/store'
  import { downloads } from '@/api/fwgl/zzxx'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import LowerEdit from '@/views/fwgl/legal/components/lowerEdit'
  // import ExecutorOptions from '@/views/fwgl/legal/components/options/executor'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import xzhtOptions from './xzht.vue'
  export default {
    name: '',
    components: { xzhtOptions, LowerEdit, projectManage, CandidateUserSelect },
    data() {
      return {
        loading: false,
        dllsData: [],
        accept: '.pdf, .doc, .docx, .xls, .xlsx,.png,.jpg,.jpeg',
        api: '/contract/uploadFileAttInfo',
        // data: {
        //   attpath: '1649657363411.xlsx',
        //   attname: '1649657363411.xlsx',
        //   token: store.getters['user/token'],
        // },
        headers: {
          token: store.getters['user/token'],
          // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        uploadlist: [],
        isDetail: false,
        value: {},
        beforeUpload: null,
        baseApi: baseURL,
        fileList: [],
        currentValue: this.value || null,
        previewUrl: undefined,
        initialed: false,
        form: {
          urgentmemo: undefined,
          zxstaffid: undefined,
          contractId: undefined,
          contractid: undefined,
          disputeno: undefined,
          disputeitem: undefined,
          disputetype: undefined,
          contractname: undefined,
          contractno: undefined,
          realname: undefined,
          plaintiff: undefined,
          defendant: undefined,
          disputecours: undefined,
          zxstaffname: undefined,
          isuegent: 1,
          whethersued: undefined,
          litigationamount: undefined,
          isattorney: 1,
          glht: 2,
          attorneyphont: undefined,
          solutionsuggestions: undefined,
          enddate1: undefined,
          attorney: undefined,
          disputeid: undefined,
        },
        rules: {
          disputeno: [
            {
              required: true,
              message: '请输入登记编号',
              trigger: 'blur',
            },
          ],
          disputeitem: [
            {
              required: true,
              message: '请输入纠纷名称',
              trigger: 'blur',
            },
          ],
          disputetype: [
            {
              required: true,
              message: '请输入纠纷类型',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请选择合同',
              trigger: 'blur',
            },
          ],
          plaintiff: [
            {
              required: true,
              message: '请输入原告',
              trigger: 'blur',
            },
          ],
          defendant: [
            {
              required: true,
              message: '请输入被告',
              trigger: 'blur',
            },
          ],
          disputecours: [
            {
              required: true,
              message: '请输入涉诉标的',
              trigger: 'blur',
            },
          ],
          whethersued: [
            {
              required: true,
              message: '请选择诉讼地位',
              trigger: 'blur',
            },
          ],
          zxstaffname: [
            {
              required: true,
              message: '请选择纠纷承办人',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
        //提交
        visible: false,
        fzforms: {
          branchStrs: [],
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzform1: {
          branchStrs: [],
        },
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzoptions: [],
        runderList: [],
        candidateData: {},
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        visible1: false,
        visible2: false,
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        status: 0,
        jurisdictionCode: 0,
        clearType: false,
      }
    },
    watch: {
      // 'form.glht': {
      //   handler(newVal, oldVal) {
      //     if (newVal === 2) this.clearContractInfo()
      //   },
      // },
    },
    computed: {
      uploadData() {
        return {
          type: 1,
          bid: this.form.disputeid,
        }
      },
    },
    created() {},
    methods: {
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      /**
       * @description: 文件上传限制
       * @param {*} file
       * @return {*}
       */
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 200
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      onExceed(file, fileList) {},
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      async showEdit(row, disabled) {
        this.form = {
          urgentmemo: undefined,
          zxstaffid: undefined,
          contractId: undefined,
          contractid: undefined,
          disputeno: undefined,
          disputeitem: undefined,
          disputetype: undefined,
          contractname: undefined,
          contractno: undefined,
          realname: undefined,
          plaintiff: undefined,
          defendant: undefined,
          disputecours: undefined,
          zxstaffname: undefined,
          isuegent: 1,
          whethersued: undefined,
          isattorney: 1,
          glht: 2,
          attorneyphont: undefined,
          solutionsuggestions: undefined,
          enddate1: undefined,
          attorney: undefined,
          disputeid: undefined,
        }
        // this.show = 0
        if (!row) {
          this.title = '添加'
          // 自动编号
          await getDisputeNo().then((res) => {
            if (res && res.code === 1) {
              this.form.disputeno = res.data.disputerno
            }
          })
        } else {
          if (disabled) {
            this.title = '查看'
            // this.show = 1
            this.isDetail = true
          } else {
            this.title = '编辑'
          }
          this.form.disputeid = row.disputeid
          this.uploadList()
          this.getDetail(row)
          this.fetchLawerList()
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      async getDetail(row) {
        this.loading = true
        const { dispute } = await disputeRegisterDetail({
          disputeId: row.disputeid,
        })
        this.loading = false
        if (dispute) {
          // this.form = dispute
          this.form.disputeno = dispute.disputeno
          this.form.disputestatus = dispute.disputestatus
          this.form.disputeitem = dispute.disputeitem
          this.form.disputetype = dispute.disputetype
          this.form.contractname = dispute.contractname
          this.form.contractno = dispute.contractno
          this.form.plaintiff = dispute.plaintiff
          this.form.defendant = dispute.defendant
          this.form.enddate1 = row.lastdealdate
          this.form.contractId = dispute.contractid
          this.form.contractid = dispute.contractid
          this.form.realname = dispute.realname
          this.form.isuegent = dispute.isuegent
          this.form.zxstaffid = dispute.disputeundertaker
          this.form.disputecours = dispute.disputecours
          this.form.zxstaffname = dispute.zxstaffname
          this.form.whethersued = dispute.whethersued
          this.form.solutionsuggestions = dispute.solutionsuggestions
          this.form.urgentmemo = dispute.urgentmemo
          this.form.legalexam = dispute.legalexam
          this.form.counselexam = dispute.counselexam
          this.form.chairmanexam = dispute.chairmanexam
          this.form.gmanexam = dispute.gmanexam
          this.form.litigationamount = dispute.litigationamount
          if (!dispute.contractid) this.form.glht = 2
          else this.form.glht = 1
        }

        if (dispute.disputestatus == 2 || dispute.disputestatus == 3) {
          const res2 = await getFlowTaskInfo({
            tableId: 20,
            formId: row.disputeid,
          })
          this.jurisdictionCode = res2.data.isFlowInfo
          if (res2.data.isFlowInfo) {
            this.flowtaskinfoflowid = res2.data.flowId
            this.fromId = row.disputeid
            this.ymFromId = res2.data.id

            const res3 = await getFaqiInfo({
              id: res2.data.id,
              flowId: res2.data.flowId,
            })
            if (res3.code == 1) {
              this.status = res3.data.dataJson.flowTaskInfo.status
            }
          }
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = {}
        this.isDetail = false
        this.clearType = true
        this.$emit('fetch-data')
        this.uploadlist = []
        this.dllsData = []
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            const bCreate = !this.form.disputeid
            const { describe, ...other } = this.form
            const func = bCreate ? caseInformationSave : caseInformationModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func({ ...other }).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.disputeid = res.data
                }
                this.$emit('fetch-data')
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })

            // this.close()
          }
        })
      },
      //附件列表
      async uploadList() {
        this.uploadlist = []
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 1,
          bid: this.form.disputeid,
        })
        this.uploadlist = data
        // this.$refs.upload.clearFiles()
        // this.$refs.upload.uploadFiles.length = 0
      },
      //附件删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 1 })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.uploadList()
        })
      },
      // 下载数据
      // downloadData(row) {
      //   const fileName = row.attname
      //   const link = document.createElement('a')
      //   link.download = fileName
      //   link.href = this.baseApi + '/download?id=' + row.attid
      //   link.style.display = 'none'
      //   document.body.appendChild(link)
      //   link.click()
      //   document.body.removeChild(link)
      // },
      async downloadData(row) {
        const data = await downloads({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 选择回调
       * @param {*} val 已选数据
       * @return {*}
       */
      handleSelected(val) {
        this.$set(this.form, 'zxstaffid', val.staffid)
        this.$set(this.form, 'zxstaffname', val.realname)
        this.form.zxstaffid = val.staffid
        this.form.zxstaffname = val.realname
      },
      handleSsjd(item) {
        console.warn('handleSsjd', item)
        this.form.contractname = item.contractname
        this.form.contractno = item.contractno
        this.form.realname = item.realname
        this.form.contractId = item.contractid
        this.form.contractid = item.contractid
      },
      clearContractInfo() {
        this.form.contractname = ''
        this.form.contractno = ''
        this.form.realname = ''
        this.form.contractId = ''
        this.form.contractid = ''
      },
      // 增加一个空行, 用于录入或显示第一行
      addLine() {
        if (!this.form.disputeid) {
          return this.$message({
            type: 'error',
            message: '请先保存表单',
          })
        }
        this.$refs['lowerEdit'].showModal({
          disputeid: this.form.disputeid,
        })
      },
      // 删除指定行
      async deleteRow(id) {
        const res = await legalAttorneyDelete({ id })
        this.fetchLawerList()
      },
      async fetchLawerList() {
        this.dllsData = []
        //
        if (!this.form.disputeid) return
        const res = await legalAttorney({ disputeid: this.form.disputeid })
        //
        this.dllsData = res.data
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */
      async getChildlistPro(val) {
        this.$set(this.form, 'zxstaffname', val[0].realname)
        this.$set(this.form, 'zxstaffid', val[0].staffid)
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      /**
       * @description: 流程提交
       * @return {*}
       */
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await ymWorkCandidates({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              flowTaskOperatorId: '',
              id: '',
            })
            this.candidateType = res.data.candidateType
            if (res.data.candidateType == 1) {
              this.fzoptions = res.data.list
              this.visible = true
              let list = []
              res.data.list.map((item) => {
                list.push({
                  value: item.nodeId,
                  label: item.nodeName,
                  hasCandidates: item.hasCandidates,
                })
              })
              this.options = list
              //保存请求人员列表的信息
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
              }
              this.candidateData = candidateData
            } else if (res.data.candidateType == 2) {
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
                nodeId: res.data.list[0].nodeId,
              }
              this.candidateData = candidateData
              this.visible2 = true
            } else {
              const wordres = await ymWorkSubmit({
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                branchStrs: this.fzforms.branchStrs
                  ? this.fzforms.branchStrs.join(',')
                  : '',
                candidateType: res.data.candidateType,
                ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
                status: this.status,
              })
              if (wordres.code === 1) {
                this.$message.success(wordres.msg)
                this.close()
                this.visible = false
              }
            }
          }
        })
      },
      currentClose() {
        this.visible = false
        this.form = {}
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      async save4() {
        if (this.fzforms.branchStrs.length == 0) {
          this.$message.warning('请选择分支')
          return
        }
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3.map((res) => {
            let str = []
            res.transferStaffId.map((item) => {
              str.push(item.id)
            })
            arr.push(str)
          })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          // tableId: this.tableId,
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs: this.fzforms.branchStrs
            ? this.fzforms.branchStrs.join(',')
            : '',
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close4()
          this.close()
        }
        // } else {
        //
        //   return false
        // }
        // })
      },
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          if (res.nodeId == e.split('~')[0]) {
            arr.push(res)
          }
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },

      resetINfo() {
        this.flowId = ''
        this.fromId = ''
        this.runderList = []
        this.formData = {
          value: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
      },
      async save1() {
        let branchStrs = ''
        this.fzform1.branchStrs.map((item) => {
          branchStrs = branchStrs + item + ','
        })
        branchStrs = branchStrs.substring(0, branchStrs.length - 1)
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3 &&
            this.formData3.map((res) => {
              let str = []
              res.transferStaffId &&
                res.transferStaffId.map((item) => {
                  str.push(item.id)
                })
              arr.push(str)
            })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs,
          candidateType: this.candidateType,
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close()
          this.visible1 = false
        }
      },
      close1() {
        this.visible1 = false
        this.resetINfo()
      },
      close2() {
        this.visible2 = false
        this.resetINfo()
      },
      async save2() {
        if (!this.formData2.transferStaffName) {
          this.$message.error('请选择候选人')
          return
        }

        let list = []
        this.formData2.transferStaffName.map((item) => {
          list.push(item.id)
        })
        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          candidateList: list.join(','),
          nodeCode: this.candidateData.nodeId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
          this.close()
        }
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .dlls {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .attorneyClass {
    display: flex;
  }
</style>
