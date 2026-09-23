<template>
  <div>
    <el-dialog
      title="风险监测指标填报"
      :visible.sync="dialogVisible"
      width="80%"
      top="5vh"
      @close="resetForm"
      :lock-scroll="false"
      :append-to-body="true"
    >
      <el-form
        ref="riskForm"
        :model="formData"
        :rules="rules"
        label-width="220px"
        class="risk-form"
        :disabled="disabled"
      >
        <el-row>
          <el-col :span="12" v-if="showMJ">
            <el-form-item
              label="密级"
              prop="secrectLevelId"
              :rules="[
                { required: true, trigger: 'change', message: '请选择密级' },
              ]"
            >
              <el-select
                v-model="formData.secrectLevelId"
                clearable
                placeholder="密级"
                style="width: 100%"
                @change="changeMJ"
              >
                <el-option
                  v-for="item in MJoption"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showMJ">
            <el-form-item label="知悉范围" prop="staffScopeNames">
              <el-input
                v-model="formData.staffScopeNames"
                readonly
                placeholder="请选择知悉范围"
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId || disabled"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 基础信息 -->
        <el-divider content-position="center">基础信息</el-divider>
        <div class="form-section">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item
                label="年度"
                prop="riskyear"
                :rules="[
                  { required: true, trigger: 'change', message: '请选择年度' },
                ]"
              >
                <el-date-picker
                  v-model="formData.riskyear"
                  type="year"
                  value-format="yyyy"
                  placeholder="选择年"
                  :style="{ width: '100%' }"
                  disabled
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="季度"
                prop="quartername"
                :rules="[
                  { required: true, trigger: 'change', message: '请选择季度' },
                ]"
              >
                <el-select
                  v-model="formData.quartername"
                  placeholder="请选择季度"
                  clearable
                  :style="{ width: '100%' }"
                  :disabled="disabled"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="填报单位" prop="linkOrgName">
                <el-input
                  v-model="formData.linkOrgName"
                  placeholder="请输入填报单位"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="填报部门" prop="linkDeptName">
                <el-input
                  v-model="formData.linkDeptName"
                  placeholder="请输入填报部门"
                  disabled
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="填报人" prop="createname">
                <el-input
                  v-model="formData.createname"
                  placeholder="请输入填报人"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="填报时间" prop="createtime">
                <el-date-picker
                  v-model="formData.createtime"
                  type="datetime"
                  placeholder="请选择填报时间"
                  style="width: 100%"
                  value-format="yyyy-MM-dd"
                  format="yyyy-MM-dd"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="notes">
                <el-input
                  disabled
                  type="textarea"
                  :rows="3"
                  v-model="formData.notes"
                  placeholder="请输入备注信息"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-divider content-position="center" class="reporting-period-divider">
          填报区间: {{ reportingPeriod }}
        </el-divider>
        <!-- 战略风险 -->
        <el-divider content-position="center">战略风险</el-divider>
        <div class="form-section">
          <div class="risk-indicator">
            <div class="indicator-title">宏观经济风险</div>
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="国内外宏观经济形势变化" prop="zlString1">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="formData.zlString1"
                    placeholder="请输入国内外宏观经济形势变化"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">政策风险</div>
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="国家及行业政策变化" prop="zlString2">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="formData.zlString2"
                    placeholder="请输入国家及行业政策变化"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">国际化经营风险</div>
            <el-row :gutter="20">
              <el-col :span="12" style="height: 80px">
                <el-form-item
                  label='被美国列入"实体清单"、受关税政策影响的子企业数量（个）'
                  prop="zlInteger1"
                >
                  <el-input
                    v-model="formData.zlInteger1"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="height: 80px">
                <el-form-item
                  label="境外中高风险地区境外资产总额"
                  prop="zlBigdecimal1"
                >
                  <el-input
                    v-model="formData.zlBigdecimal1"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label="境外中高风险地区资产占比"
                  prop="zlBigdecimal2"
                >
                  <el-input
                    v-model="formData.zlBigdecimal2"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="境外重大建设项目逾期数量"
                  prop="zlInteger2"
                >
                  <el-input
                    v-model="formData.zlInteger2"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="汇率损失金额"
                  prop="zlBigdecimal3"
                  required
                >
                  <el-input
                    v-model="formData.zlBigdecimal3"
                    placeholder="请输入金额"
                    class="required-input"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="境外重大法律诉讼案件数量"
                  prop="zlInteger3"
                >
                  <el-input
                    v-model="formData.zlInteger3"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label="境外重大法律诉讼案件涉案金额"
                  prop="zlBigdecimal4"
                >
                  <el-input
                    v-model="formData.zlBigdecimal4"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="境外重大合规案件（如被世行禁止参与投标、境外腐败）"
                  prop="zlInteger4"
                >
                  <el-input
                    v-model="formData.zlInteger4"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label="境外重大合规案件涉案金额"
                  prop="zlBigdecimal5"
                >
                  <el-input
                    v-model="formData.zlBigdecimal5"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">改革与业务转型风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="主要业务板块收入"
                  prop="zlBigdecimal6"
                  required
                >
                  <el-input
                    v-model="formData.zlBigdecimal6"
                    placeholder="请输入金额"
                    class="required-input"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label="主要业务收入占总收入比重"
                  prop="zlBigdecimal7"
                >
                  <el-input
                    v-model="formData.zlBigdecimal7"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">科技创新风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="研发投入"
                  prop="zlBigdecimal8"
                  required
                >
                  <el-input
                    v-model="formData.zlBigdecimal8"
                    placeholder="请输入金额"
                    class="required-input"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="研发投入占总收入比重" prop="zlBigdecimal9">
                  <el-input
                    v-model="formData.zlBigdecimal9"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="重大科技项目逾期数量" prop="zlInteger5">
                  <el-input
                    v-model="formData.zlInteger5"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 财务风险 -->
        <el-divider content-position="center">财务风险</el-divider>
        <div class="form-section">
          <div class="risk-indicator">
            <div class="indicator-title">金融及金融衍生品业务风险</div>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="对外担保业务违约事项数量"
                  prop="cwInteger1"
                >
                  <el-input
                    v-model="formData.cwInteger1"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="对外担保业务违约事项金额"
                  prop="cwBigdecimal2"
                >
                  <el-input
                    v-model="formData.cwBigdecimal2"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="融资租赁业务违约数量" prop="cwInteger2">
                  <el-input
                    v-model="formData.cwInteger2"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="融资租赁业务违约金额" prop="cwBigdecimal3">
                  <el-input
                    v-model="formData.cwBigdecimal3"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="债券、股票质押回购数量" prop="cwInteger3">
                  <el-input
                    v-model="formData.cwInteger3"
                    placeholder="请输入数量"
                  >
                    <template slot="append">笔</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="债券、股票质押回购金额"
                  prop="cwBigdecimal4"
                >
                  <el-input
                    v-model="formData.cwBigdecimal4"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="信托业务违约数量" prop="cwInteger4">
                  <el-input
                    v-model="formData.cwInteger4"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="信托业务违约金额" prop="cwBigdecimal5">
                  <el-input
                    v-model="formData.cwBigdecimal5"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="保理业务违约数量" prop="cwInteger5">
                  <el-input
                    v-model="formData.cwInteger5"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="保理业务违约金额" prop="cwBigdecimal6">
                  <el-input
                    v-model="formData.cwBigdecimal6"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="衍生品盈亏金额" prop="cwBigdecimal1">
                  <el-input
                    v-model="formData.cwBigdecimal1"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">债务风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="流动比率" prop="cwBigdecimal7">
                  <el-input
                    v-model="formData.cwBigdecimal7"
                    placeholder="请输入流动比率"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="资产负债率" prop="cwBigdecimal8">
                  <el-input
                    v-model="formData.cwBigdecimal8"
                    placeholder="请输入资产负债率"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">现金流风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="现金流动负债比率"
                  prop="cwBigdecimal9"
                  required
                >
                  <el-input
                    v-model="formData.cwBigdecimal9"
                    placeholder="请输入百分比"
                    class="required-input"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="两金（应收账款和存货）总金额"
                  prop="cwBigdecimal10"
                >
                  <el-input
                    v-model="formData.cwBigdecimal10"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="两金（应收账款和存货）占流动资产比重"
                  prop="cwBigdecimal11"
                >
                  <el-input
                    v-model="formData.cwBigdecimal11"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 市场风险 -->
        <el-divider content-position="center">市场风险</el-divider>
        <div class="form-section">
          <div class="risk-indicator">
            <div class="indicator-title">市场竞争风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="主要产品价格下降"
                  prop="scInteger1"
                  required
                >
                  <el-input
                    v-model="formData.scInteger1"
                    placeholder="请输入数量"
                    class="required-input"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最大降幅" prop="scBigdecimal1">
                  <el-input
                    v-model="formData.scBigdecimal1"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="主要产品市场占有率下降" prop="scInteger2">
                  <el-input
                    v-model="formData.scInteger2"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最大降幅" prop="scBigdecimal2">
                  <el-input
                    v-model="formData.scBigdecimal2"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">客户信用风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="账龄三年及以上的应收账款金额"
                  prop="scBigdecimal3"
                  required
                >
                  <el-input
                    v-model="formData.scBigdecimal3"
                    placeholder="请输入金额"
                    class="required-input"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="占应收账款比重" prop="scBigdecimal4">
                  <el-input
                    v-model="formData.scBigdecimal4"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="逾期应收账款" prop="scBigdecimal5">
                  <el-input
                    v-model="formData.scBigdecimal5"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="占应收账款比重" prop="scBigdecimal6">
                  <el-input
                    v-model="formData.scBigdecimal6"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 运营风险 -->
        <el-divider content-position="center">运营风险</el-divider>
        <div class="form-section">
          <div class="risk-indicator">
            <div class="indicator-title">经营效益风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="亏损子企业" prop="yyInteger1">
                  <el-input
                    v-model="formData.yyInteger1"
                    placeholder="请输入数量"
                  >
                    <template slot="append">户</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="亏损金额" prop="yyBigdecimal1">
                  <el-input
                    v-model="formData.yyBigdecimal1"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">投资风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="非主业项目投资"
                  prop="yyBigdecimal2"
                  required
                >
                  <el-input
                    v-model="formData.yyBigdecimal2"
                    placeholder="请输入金额"
                    class="required-input"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="占总投资额比重" prop="yyBigdecimal3">
                  <el-input
                    v-model="formData.yyBigdecimal3"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="年度投资计划完成率（季度实际投资额/全年计划投资额）"
                  prop="yyBigdecimal4"
                >
                  <el-input
                    v-model="formData.yyBigdecimal4"
                    placeholder="请输入百分比"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">安全、环保、质量风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="重大安全生产事故数量"
                  prop="yyInteger2"
                  required
                >
                  <el-input
                    v-model="formData.yyInteger2"
                    placeholder="请输入数量"
                    class="required-input"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="重大及以上突发环境事件数量"
                  prop="yyInteger3"
                  required
                >
                  <el-input
                    v-model="formData.yyInteger3"
                    placeholder="请输入数量"
                    class="required-input"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">舆情风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="重大舆情事件数量" prop="yyInteger4">
                  <el-input
                    v-model="formData.yyInteger4"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <div class="risk-indicator">
            <div class="indicator-title">采购与供应链管理风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="出现重要产品断供的子企业"
                  prop="yyInteger5"
                >
                  <el-input
                    v-model="formData.yyInteger5"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div class="risk-indicator">
            <div class="indicator-title">工程项目管理风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="境内重大建设项目逾期数量"
                  prop="yyInteger6"
                >
                  <el-input
                    v-model="formData.yyInteger6"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 法律风险 -->
        <el-divider content-position="center">法律风险</el-divider>
        <div class="form-section">
          <div class="risk-indicator">
            <div class="indicator-title">合规风险</div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="境内重大法律诉讼案件" prop="flInteger2">
                  <el-input
                    v-model="formData.flInteger2"
                    placeholder="请输入数量"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="境内重大法律诉讼案件金额"
                  prop="flBigdecimal1"
                >
                  <el-input
                    v-model="formData.flBigdecimal1"
                    placeholder="请输入金额"
                  >
                    <template slot="append">万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label-class="required-label"
                  label="重大监管处罚数量"
                  prop="flInteger1"
                  required
                >
                  <el-input
                    v-model="formData.flInteger1"
                    placeholder="请输入数量"
                    class="required-input"
                  >
                    <template slot="append">个</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 其他风险 -->
        <el-divider content-position="center">其他风险</el-divider>
        <div class="form-section">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item
                label="其他对企业经营发展造成重大影响的风险"
                prop="qtString1"
              >
                <el-input
                  v-model="formData.qtString1"
                  type="textarea"
                  :rows="4"
                  placeholder="请描述其他风险情况"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    saveOrUpdate,
    detail,
    getRiskMonDeptList,
  } from '@/api/risk/monitoringFill'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  export default {
    components: { ZXPerson },
    data() {
      return {
        dialogVisible: false,
        editType: '', // 'fill' 或 'detail'
        currentRow: null,
        disabled: false,
        formData: {
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          assId: undefined,
          assname: undefined,
          // 基础信息字段
          id: null,
          riskyear: '',
          quartername: '',
          notes: '',
          monitorId: null,
          createstaffid: null,
          createname: '',
          createtime: null,
          contact: '',
          secrectlevelid: null,
          staffscopeids: '',
          staffscopenames: '',
          linkOrgId: null,
          linkOrgName: '',
          linkDeptId: null,
          linkDeptName: null,
          toReportDate: null,
          reportStaffid: '',
          reportStaffName: '',
          reportstatus: 0,

          // 战略风险字段 (ZL开头)
          zlString1: '', // 国内外宏观经济形势变化
          zlString2: '', // 国家及行业政策变化
          zlInteger1: '', // 被美国列入"实体清单"、受关税政策影响的子企业数量
          zlInteger2: '', // 境外重大建设项目逾期数量
          zlInteger3: '', // 境外重大法律诉讼案件数量
          zlInteger4: '', // 境外重大合规案件数量
          zlInteger5: '', // 重大科技项目逾期数量
          zlBigdecimal1: '', // 境外中高风险地区境外资产总额
          zlBigdecimal2: '', // 境外中高风险地区资产占比
          zlBigdecimal3: '', // 汇率损失金额
          zlBigdecimal4: '', // 境外重大法律诉讼案件涉案金额
          zlBigdecimal5: '', // 境外重大合规案件涉案金额
          zlBigdecimal6: '', // 主要业务板块收入
          zlBigdecimal7: '', // 主要业务收入占总收入比重
          zlBigdecimal8: '', // 研发投入
          zlBigdecimal9: '', // 研发投入占总收入比重

          // 财务风险字段 (CW开头)
          cwInteger1: '', // 对外担保业务违约事项数量
          cwInteger2: '', // 融资租赁业务违约数量
          cwInteger3: '', // 债券、股票质押回购数量
          cwInteger4: '', // 信托业务违约数量
          cwInteger5: '', // 保理业务违约数量
          cwBigdecimal1: '', // 衍生品盈亏金额
          cwBigdecimal2: '', // 对外担保业务违约事项金额
          cwBigdecimal3: '', // 融资租赁业务违约金额
          cwBigdecimal4: '', // 债券、股票质押回购金额
          cwBigdecimal5: '', // 信托业务违约金额
          cwBigdecimal6: '', // 保理业务违约金额
          cwBigdecimal7: '', // 流动比率
          cwBigdecimal8: '', // 资产负债率
          cwBigdecimal9: '', // 现金流动负债比率
          cwBigdecimal10: '', // 两金（应收账款和存货）总金额
          cwBigdecimal11: '', // 两金（应收账款和存货）占流动资产比重

          // 市场风险字段 (SC开头)
          scInteger1: '', // 主要产品价格下降
          scInteger2: '', // 主要产品市场占有率下降
          scBigdecimal1: '', // 主要产品价格下降最大降幅
          scBigdecimal2: '', // 主要产品市场占有率下降最大降幅
          scBigdecimal3: '', // 账龄三年及以上的应收账款金额
          scBigdecimal4: '', // 账龄三年及以上的应收账款占应收账款比重
          scBigdecimal5: '', // 逾期应收账款
          scBigdecimal6: '', // 逾期应收账款占应收账款比重

          // 运营风险字段 (YY开头)
          yyInteger1: '', // 亏损子企业
          yyInteger2: '', // 重大安全生产事故数量
          yyInteger3: '', // 重大及以上突发环境事件数量
          yyInteger4: '', // 重大舆情事件数量
          yyInteger5: '', // 出现重要产品断供的子企业数量
          yyInteger6: '', // 境内重大建设项目逾期数量
          yyBigdecimal1: '', // 亏损金额
          yyBigdecimal2: '', // 非主业项目投资
          yyBigdecimal3: '', // 非主业项目投资占总投资额比重
          yyBigdecimal4: '', // 年度投资计划完成率

          // 法律风险字段 (FL开头)
          flInteger1: '', // 重大监管处罚数量
          flInteger2: '', // 境内重大法律诉讼案件数量
          flBigdecimal1: '', // 境内重大法律诉讼案件涉案金额

          // 其他风险字段 (QT开头)
          qtString1: '', // 其他对企业经营发展造成重大影响的风险
        },
        rules: {
          // 战略风险验证规则
          zlString1: [
            {
              required: true,
              message: '请输入国内外宏观经济形势变化',
              trigger: ['blur', 'change'],
            },
          ],
          zlString2: [
            {
              required: true,
              message: '请输入国家及行业政策变化',
              trigger: ['blur', 'change'],
            },
          ],
          zlInteger1: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlInteger2: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlInteger3: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlInteger4: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlInteger5: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal1: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal2: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal3: [
            {
              required: true,
              message: '请输入汇率损失金额',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal4: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal5: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal6: [
            {
              required: true,
              message: '请输入主要业务板块收入',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal7: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal8: [
            {
              required: true,
              message: '请输入研发投入',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          zlBigdecimal9: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],

          // 财务风险验证规则
          cwInteger1: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwInteger2: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwInteger3: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwInteger4: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwInteger5: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal1: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal2: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal3: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal4: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal5: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal6: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal7: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal8: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal9: [
            {
              required: true,
              message: '请输入现金流动负债比率',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal10: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          cwBigdecimal11: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],

          // 市场风险验证规则
          scInteger1: [
            {
              required: true,
              message: '请输入主要产品价格下降数量',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scInteger2: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scBigdecimal1: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scBigdecimal2: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scBigdecimal3: [
            {
              required: true,
              message: '请输入账龄三年及以上的应收账款金额',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scBigdecimal4: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scBigdecimal5: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          scBigdecimal6: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],

          // 运营风险验证规则
          yyInteger1: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyInteger2: [
            {
              required: true,
              message: '请输入重大安全生产事故数量',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyInteger3: [
            {
              required: true,
              message: '请输入重大及以上突发环境事件数量',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyInteger4: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyInteger5: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyInteger6: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyBigdecimal1: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyBigdecimal2: [
            {
              required: true,
              message: '请输入非主业项目投资',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyBigdecimal3: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          yyBigdecimal4: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],

          // 法律风险验证规则
          flInteger1: [
            {
              required: true,
              message: '请输入重大监管处罚数量',
              trigger: ['blur', 'change'],
            },
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          flInteger2: [
            {
              pattern: /^\d+$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
          flBigdecimal1: [
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效数字',
              trigger: ['blur', 'change'],
            },
          ],
        },
        options: [
          {
            value: '一季度',
            label: '一季度',
          },
          {
            value: '二季度',
            label: '二季度',
          },
          {
            value: '三季度',
            label: '三季度',
          },
          {
            value: '四季度',
            label: '四季度',
          },
        ],
        showMJ: false,
        MJoption: [],
      }
    },
    computed: {
      // 根据年度和季度计算填报区间
      reportingPeriod() {
        if (!this.formData.riskyear || !this.formData.quartername) {
          return '请选择年度和季度'
        }

        const year = parseInt(this.formData.riskyear)
        const quarter = this.formData.quartername

        let startDate, endDate

        switch (quarter) {
          case '一季度':
            startDate = `${year}.1.1`
            endDate = `${year}.3.31`
            break
          case '二季度':
            startDate = `${year}.1.1`
            endDate = `${year}.6.30`
            break
          case '三季度':
            startDate = `${year}.1.1`
            endDate = `${year}.9.30`
            break
          case '四季度':
            startDate = `${year}.1.1`
            endDate = `${year + 1}.12.31`
            break
          default:
            return '请选择有效季度'
        }

        return `${startDate} - ${endDate}`
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('riskIndicatorCreation')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
            this.formData.assId = undefined
            this.formData.assname = undefined
          }
        }
      },

      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      // 显示填报弹窗
      async showEdit(row, type) {
        this.getRiskMonDeptList(row)
        this.dialogVisible = true
        this.editType = type
        this.currentRow = row
        this.disabled = type === 'detail'
        if ((type === 'detail' || type === 'fill') && row.id) {
          const { data } = await detail({ id: row.id })
          Object.assign(this.formData, data)
          // 处理年度格式
          if (data.riskyear) {
            this.$set(this.formData, 'riskyear', String(data.riskyear))
          }
        } else {
          // 新增模式，设置基础信息
          const { riskyear, quartername, monitorId } = row
          this.formData.riskyear = riskyear || new Date().getFullYear()
          this.formData.quartername = quartername || ''
          this.formData.monitorId = monitorId || null
          this.disabled = false
        }
      },
      //获取填写的字段数据
      async getRiskMonDeptList(row) {
        const {
          data: { data },
        } = await getRiskMonDeptList({ id: row.id })
      },
      submitForm() {
        this.$refs.riskForm.validate((valid) => {
          if (valid) {
            const submitData = {
              ...this.formData,
              cwBigdecimal11: Number(this.formData.cwBigdecimal11),
              // 如果是编辑模式，保留ID
              ...(this.currentRow && this.currentRow.id
                ? { id: this.currentRow.id }
                : {}),
            }
            // 调用保存接口
            saveOrUpdate(submitData)
              .then((response) => {
                const { code, msg } = response
                if (code === 1) {
                  this.$message.success('保存成功！')
                  this.dialogVisible = false
                  // 触发父组件刷新数据
                  this.$emit('fetchData')
                } else {
                  this.$message.error(msg || '提交失败')
                }
              })
              .catch((error) => {
                console.error('提交失败:', error)
                this.$message.error('提交失败，请重试')
              })
          }
        })
      },
      resetForm() {
        if (this.$refs.riskForm) {
          this.$refs.riskForm.resetFields()
        }
        this.formData = {
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          assId: undefined,
          assname: undefined,
          // 基础信息字段
          id: null,
          riskyear: '',
          quartername: '',
          notes: '',
          monitorId: null,
          createstaffid: null,
          createname: '',
          createtime: null,
          contact: '',
          status: 0,
          secrectlevelid: null,
          staffscopeids: '',
          staffscopenames: '',
          linkOrgId: null,
          linkOrgName: '',
          linkDeptId: null,
          linkDeptName: null,
          toReportDate: null,
          reportStaffid: '',
          reportStaffName: '',
          reportstatus: 0,

          // 战略风险字段 (ZL开头)
          zlString1: '', // 国内外宏观经济形势变化
          zlString2: '', // 国家及行业政策变化
          zlInteger1: '', // 被美国列入"实体清单"、受关税政策影响的子企业数量
          zlInteger2: '', // 境外重大建设项目逾期数量
          zlInteger3: '', // 境外重大法律诉讼案件数量
          zlInteger4: '', // 境外重大合规案件数量
          zlInteger5: '', // 重大科技项目逾期数量
          zlBigdecimal1: '', // 境外中高风险地区境外资产总额
          zlBigdecimal2: '', // 境外中高风险地区资产占比
          zlBigdecimal3: '', // 汇率损失金额
          zlBigdecimal4: '', // 境外重大法律诉讼案件涉案金额
          zlBigdecimal5: '', // 境外重大合规案件涉案金额
          zlBigdecimal6: '', // 主要业务板块收入
          zlBigdecimal7: '', // 主要业务收入占总收入比重
          zlBigdecimal8: '', // 研发投入
          zlBigdecimal9: '', // 研发投入占总收入比重

          // 财务风险字段 (CW开头)
          cwInteger1: '', // 对外担保业务违约事项数量
          cwInteger2: '', // 融资租赁业务违约数量
          cwInteger3: '', // 债券、股票质押回购数量
          cwInteger4: '', // 信托业务违约数量
          cwInteger5: '', // 保理业务违约数量
          cwBigdecimal1: '', // 衍生品盈亏金额
          cwBigdecimal2: '', // 对外担保业务违约事项金额
          cwBigdecimal3: '', // 融资租赁业务违约金额
          cwBigdecimal4: '', // 债券、股票质押回购金额
          cwBigdecimal5: '', // 信托业务违约金额
          cwBigdecimal6: '', // 保理业务违约金额
          cwBigdecimal7: '', // 流动比率
          cwBigdecimal8: '', // 资产负债率
          cwBigdecimal9: '', // 现金流动负债比率
          cwBigdecimal10: '', // 两金（应收账款和存货）总金额
          cwBigdecimal11: '', // 两金（应收账款和存货）占流动资产比重

          // 市场风险字段 (SC开头)
          scInteger1: '', // 主要产品价格下降
          scInteger2: '', // 主要产品市场占有率下降
          scBigdecimal1: '', // 主要产品价格下降最大降幅
          scBigdecimal2: '', // 主要产品市场占有率下降最大降幅
          scBigdecimal3: '', // 账龄三年及以上的应收账款金额
          scBigdecimal4: '', // 账龄三年及以上的应收账款占应收账款比重
          scBigdecimal5: '', // 逾期应收账款
          scBigdecimal6: '', // 逾期应收账款占应收账款比重

          // 运营风险字段 (YY开头)
          yyInteger1: '', // 亏损子企业
          yyInteger2: '', // 重大安全生产事故数量
          yyInteger3: '', // 重大及以上突发环境事件数量
          yyInteger4: '', // 重大舆情事件数量
          yyInteger5: '', // 出现重要产品断供的子企业数量
          yyInteger6: '', // 境内重大建设项目逾期数量
          yyBigdecimal1: '', // 亏损金额
          yyBigdecimal2: '', // 非主业项目投资
          yyBigdecimal3: '', // 非主业项目投资占总投资额比重
          yyBigdecimal4: '', // 年度投资计划完成率

          // 法律风险字段 (FL开头)
          flInteger1: '', // 重大监管处罚数量
          flInteger2: '', // 境内重大法律诉讼案件数量
          flBigdecimal1: '', // 境内重大法律诉讼案件涉案金额

          // 其他风险字段 (QT开头)
          qtString1: '', // 其他对企业经营发展造成重大影响的风险
        }

        // 重置禁用状态
        this.$nextTick(() => {
          const inputs = document.querySelectorAll(
            '.risk-form input, .risk-form select, .risk-form textarea'
          )
          inputs.forEach((input) => {
            input.removeAttribute('disabled')
          })
        })
      },
    },
  }
</script>

<style scoped>
  .divider-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 40px;
    margin-top: 10px;
  }

  .vertical-divider {
    width: 1px;
    height: 100%;
    background-color: #dcdfe6;
  }

  .form-section {
    margin-bottom: 20px;
  }

  .risk-indicator {
    margin-bottom: 15px;
  }

  .indicator-title {
    font-weight: bold;
    margin-bottom: 10px;
    color: #606266;
  }

  .reporting-period-divider {
    color: red !important;
  }

  .reporting-period-divider .el-divider__text {
    color: red !important;
    font-weight: bold;
  }
</style>
