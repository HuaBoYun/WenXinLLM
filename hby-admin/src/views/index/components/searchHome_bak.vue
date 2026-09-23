<template>
  <div style="position: relative">
    <div class="appContent" v-if="type == 0">
      <div class="textareaBox">
        <div class="textareaBoxMenu"><i class="el-icon-menu"></i></div>
        <div class="textareaContainer">
          <textarea
            class="textarea"
            ref="autoresizing"
            @input="resize"
            v-model="searchValue"
            placeholder="请输入内容"
          ></textarea>
        </div>
        <div class="textareaBoxSend"><i class="el-icon-position"></i></div>
      </div>
      <div class="home_title">问心AI</div>
      <div class="boxContent">
        <div class="boxContentItem" v-for="(i, index) in newList" :key="index">
          <div class="itemTitleBox">
            <div class="itemTitleBoxBg">
              <div class="itemTitleBoxIcon"><i :class="i.icon"></i></div>
              <div class="itemTitleBoxTitle">{{ i.nav }}</div>
              <div class="itemTitleBoxLine">/</div>
              <div class="itemTitleBoxBlurb">{{ i.navTips }}</div>
              <div class="itemTitleBoxMore">
                进入专区
                <div class="itemTitleBoxRight">></div>
              </div>
            </div>
          </div>
          <div class="itemListBox">
            <div
              class="contentNavItem"
              v-for="(m, n) in i.children"
              :key="n"
              @click="type = m.id"
            >
              <div class="contentNavItemIcon"><i :class="m.icon"></i></div>
              <div class="contentNavItemTitle">{{ m.name }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="lunwengaixie_box" v-if="type == 1">
      <div class="lunwengaixie_box_left">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-edit"></i>
          论文改写
        </p>
        <div class="lunwengaixie_box_left_content">
          <el-input
            type="textarea"
            placeholder="在此处输入或粘贴您的改写内容"
            v-model="textarea1"
          ></el-input>
          <div class="lunwengaixie_box_left_content_number">
            <div class="lunwengaixie_box_left_content_number_inside">
              <span
                :style="{ color: textarea1.length > 700 ? 'red' : '#a1a1a1' }"
              >
                {{ textarea1.length }}/700
              </span>
              <i class="el-icon-delete" @click="textarea1 = ''"></i>
            </div>
          </div>
        </div>
        <div class="lunwengaixie_box_left_remark_box">
          <el-input
            type="textarea"
            placeholder="请输入您的改写要求"
            v-model="textarea2"
          ></el-input>
          <div class="lunwengaixie_box_left_remark_box_tag">
            <div class="tag_box">
              <span
                class=""
                v-for="(item, index) in tagList"
                :key="index"
                @click="textarea2 = item"
              >
                {{ item }}
              </span>
            </div>
            <div class="lunwengaixie_tag_number">
              <span
                :style="{ color: textarea2.length > 100 ? 'red' : '#a1a1a1' }"
              >
                {{ textarea2.length }}/100
              </span>
              <el-button size="small" type="primary" @click="submitEidt">
                提交改写
              </el-button>
            </div>
          </div>
        </div>
      </div>
      <div class="lunwengaixie_box_right">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-caret-right"></i>
          改写结果
        </p>
        <el-input
          type="textarea"
          placeholder="在左侧输入要改写的内容，提交后将为你改写"
          v-model="textarea3"
          disabled
        ></el-input>
      </div>
    </div>
    <div class="lunwenzhixie_box" v-if="type == 2">
      <div class="lunwenzhixie_box_left">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-edit"></i>
          论文致谢
        </p>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          class="demo-ruleForm lunwenzhixie_form"
        >
          <el-form-item label="论文标题：" prop="title">
            <el-input
              size="large"
              v-model="ruleForm.title"
              placeholder="在此处输入论文标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="导师姓名：" prop="name">
            <el-input
              size="large"
              v-model="ruleForm.name"
              placeholder="在此处输入导师姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="研究机构或实验室：" prop="roomName">
            <el-input
              size="large"
              v-model="ruleForm.roomName"
              placeholder="在此处输入研究机构或实验室名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="项目或基金：" prop="project">
            <el-input
              size="large"
              v-model="ruleForm.project"
              placeholder="在此处输入具体的项目或基金名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="其他补充" prop="remark">
            <el-input
              size="large"
              type="textarea"
              maxlength="500"
              show-word-limit
              v-model="ruleForm.remark"
              placeholder="在此处输入其他补充内容或生成要求"
            ></el-input>
          </el-form-item>
          <el-form-item class="lunwenzhixie_form_btn">
            <div class="lunwenzhixie_form_btn_position_left">
              <span>致谢字数：</span>
              <el-select
                size="small"
                v-model="sizeNumber"
                placeholder="请选择活动区域"
                style="width: 120px"
              >
                <el-option label="300字以内" value="300"></el-option>
                <el-option label="300-500字" value="500"></el-option>
                <el-option label="500字以上" value="1000"></el-option>
              </el-select>
            </div>
            <div class="lunwenzhixie_form_btn_box">
              <el-button
                type="primary"
                size="small"
                @click="submitForm('ruleForm')"
              >
                生成致谢
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div class="lunwenzhixie_box_right">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-caret-right"></i>
          论文致谢结果
        </p>
        <el-input
          type="textarea"
          placeholder="在左侧输入内容并提交，将为你生成论文致谢"
          v-model="textarea8"
        ></el-input>
      </div>
    </div>
    <div class="lunwenzhixie_box" v-if="type == 3">
      <div class="lunwenzhixie_box_left">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-edit"></i>
          论文提纲
        </p>
        <el-form
          :model="ruleFormTiGang"
          :rules="rulesTiGang"
          ref="ruleFormTiGang"
          class="demo-ruleForm lunwenzhixie_form"
        >
          <el-form-item label="论文标题：" prop="title">
            <el-input
              size="large"
              v-model="ruleFormTiGang.title"
              placeholder="在此处输入论文标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="生成要求" prop="require">
            <el-input
              size="large"
              type="textarea"
              maxlength="500"
              show-word-limit
              v-model="ruleFormTiGang.require"
              placeholder="在此处输入生成要求"
            ></el-input>
          </el-form-item>
          <el-form-item label="参考资料：" prop="remark">
            <el-input
              size="large"
              type="textarea"
              maxlength="500"
              show-word-limit
              v-model="ruleFormTiGang.remark"
              placeholder="在此处黏贴参考资料"
            ></el-input>
          </el-form-item>
          <el-form-item class="lunwenzhixie_form_btn">
            <div class="lunwenzhixie_form_btn_box">
              <el-button
                type="primary"
                size="small"
                @click="submitFormTiGang('ruleFormTiGang')"
              >
                生成提纲
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div class="lunwenzhixie_box_right">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-caret-right"></i>
          论文提纲结果
        </p>
        <el-input
          type="textarea"
          placeholder="在左侧输入要论文提纲的内容后，将自动为你生成"
          v-model="textarea7"
        ></el-input>
      </div>
    </div>

    <div class="lunwenzhixie_box" v-if="type == 4">
      <div class="lunwenzhixie_box_left">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-edit"></i>
          论文摘要
        </p>
        <el-form
          :model="ruleFormZhaiyao"
          :rules="rulesZhaiyao"
          ref="ruleFormZhaiyao"
          class="demo-ruleForm lunwenzhixie_form"
        >
          <el-form-item label="生成模式" prop="model">
            <el-radio-group
              v-model="rulesZhaiyao.radio"
              size="large"
              style="width: 100%"
            >
              <el-radio label="1" border>根据标题生成摘要</el-radio>
              <el-radio label="2" border>根据文字提炼摘要</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="论文标题" prop="title">
            <el-input
              size="large"
              v-model="ruleFormZhaiyao.title"
              placeholder="在此处输入论文标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="生成要求（选填）" prop="remark">
            <el-input
              size="large"
              type="textarea"
              maxlength="100"
              show-word-limit
              v-model="ruleFormZhaiyao.remark"
              placeholder="在此处输入摘要生成要求"
            ></el-input>
          </el-form-item>
          <el-form-item class="lunwenzhaiyao_form_btn">
            <div class="lunwenzhixie_form_btn_position_left">
              <span>摘要字数：</span>
              <el-select
                size="small"
                v-model="sizeNumber"
                placeholder="请选择活动区域"
                style="width: 120px"
              >
                <el-option label="300字以内" value="300"></el-option>
                <el-option label="300-500字" value="500"></el-option>
                <el-option label="500字以上" value="1000"></el-option>
              </el-select>
            </div>
            <div class="lunwenzhixie_form_btn_box">
              <el-button
                type="primary"
                size="small"
                @click="submitFormZhaiyao('ruleFormZhaiyao')"
              >
                立即生成
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div class="lunwenzhixie_box_right">
        <p class="lunwengaixie_box_title">
          <i class="el-icon-caret-right"></i>
          摘要内容
        </p>
        <el-input
          type="textarea"
          placeholder="在左侧输入内容并提交，将为您生成论文摘要"
          v-model="textarea3"
        ></el-input>
      </div>
    </div>
    <div class="lunwenxuxie_box" v-if="type == 5">
      <div class="lunwenxuxie_content">
        <div class="lunwenxuxie_content_box">
          <p class="lunwengaixie_box_title">
            <i class="el-icon-edit"></i>
            论文续写
          </p>
          <div class="lunwenxuxie_box_content">
            <el-input
              type="textarea"
              placeholder="请此处输入或粘贴你想要续写的内容"
              v-model="textarea5_1"
            ></el-input>
            <div
              class="lunwengaixie_box_left_content_number lunwenxuxie_box_left_content_number"
            >
              <div class="lunwengaixie_box_left_content_number_inside">
                <span
                  :style="{
                    color: textarea5_1.length > 1000 ? 'red' : '#a1a1a1',
                  }"
                >
                  {{ textarea5_1.length }}/1000
                </span>
                <i class="el-icon-delete" @click="textarea5_1 = ''"></i>
              </div>
            </div>
          </div>
          <div class="lunwenxuxie_box_title">
            <div>
              <i class="el-icon-edit"></i>
              续写模型：
            </div>
            <div>
              <span>续写字数：</span>
              <el-select
                size="small"
                v-model="sizeNumber1"
                placeholder="请选择活动区域"
                style="width: 120px"
              >
                <el-option label="200字以内" value="300"></el-option>
                <el-option label="200-500字" value="500"></el-option>
                <el-option label="500字以上" value="1000"></el-option>
              </el-select>
            </div>
          </div>
          <div class="lunwengaixie_box_left_remark_box">
            <el-input
              type="textarea"
              placeholder="请输入您的改写要求"
              v-model="textarea5_2"
            ></el-input>
            <div class="lunwengaixie_box_left_remark_box_tag">
              <div class="tag_box">
                <span
                  class=""
                  v-for="(item, index) in tagList1"
                  :key="index"
                  @click="textarea5_2 = item"
                >
                  {{ item }}
                </span>
              </div>
              <div class="lunwengaixie_tag_number">
                <span
                  :style="{
                    color: textarea5_2.length > 100 ? 'red' : '#a1a1a1',
                  }"
                >
                  {{ textarea5_2.length }}/100
                </span>
                <el-button
                  size="small"
                  type="primary"
                  @click="submit5"
                  :disabled="textarea5_1.length < 1"
                >
                  提交改写
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="model_6" v-if="type == 6">
      <div class="model_6_box">
        <div class="model_6_upload">
          <div class="model_6_upload_box">
            <el-upload
              class="upload-demo"
              drag
              action="https://jsonplaceholder.typicode.com/posts/"
              multiple
              style="width: 100%; height: 100%"
            >
              <!-- <i class="el-icon-upload"></i> -->
              <div class="el-upload__text">
                <div>上传或拖入要翻译的文档</div>
                <el-button type="primary" icon="el-icon-upload">
                  上传文件
                </el-button>
                <div>
                  支持格式：
                  <i
                    class="el-icon-document"
                    style="color: rgb(245, 115, 115); margin-right: 5px"
                  ></i>
                  pdf（支持扫描文件）
                  <i
                    class="el-icon-document"
                    style="color: rgb(60, 146, 239); margin-right: 5px"
                  ></i>
                  word
                  <el-divider direction="vertical"></el-divider>
                  支持语言：中、英互译
                  <el-divider direction="vertical"></el-divider>
                  支持大小: 15MB、500页以内
                </div>
              </div>
            </el-upload>
          </div>
        </div>
        <div class="model_6_introduce">
          <div class="model_6_introduce_left">
            <div
              v-for="(item, index) in model_6_list"
              :key="index"
              :class="`model_6_introduce_left_item ${
                model_6_list_active == index
                  ? 'model_6_introduce_left_item_active'
                  : ''
              }`"
              @click="model_6_list_active = index"
            >
              <div class="model_6_introduce_left_item_title">
                <i class="el-icon-film"></i>
                {{ item.title }}
              </div>
              <div class="model_6_introduce_left_item_content">
                {{ item.content }}
              </div>
            </div>
          </div>
          <!-- <div
              :class="`model_6_introduce_right ${
                model_6_list_active == 2 ? 'model_6_introduce_right_size' : ''
              }`"
              :style="[
                {
                  backgroundImage: `url(${require(`../img/${
                    model_6_list_active + 1
                  }.png`)})`,
                },
              ]"
            ></div> -->
        </div>
      </div>
    </div>
    <div class="model_7" v-if="type == 7">
      <div class="model_7_box">
        <div class="model_7_title">
          朋友群文案
          <!-- <img src="../img/friend.png" alt="" /> -->
        </div>
        <el-form
          :model="ruleForm_model_7"
          :rules="rules_model_7"
          ref="ruleForm"
          class="demo-ruleForm"
        >
          <el-form-item
            v-for="(item, index) in model_7_formList"
            :key="index"
            :label="item.label + ':'"
            :prop="item.props"
          >
            <el-input
              v-model="ruleForm_model_7[item.props]"
              v-if="item.type == 'input'"
              :placeholder="item.placeholder"
              size="large"
            />
            <el-select
              v-model="ruleForm_model_7[item.props]"
              v-if="item.type == 'select'"
              :placeholder="item.placeholder"
              size="large"
              style="width: 100%"
            />
            <el-input
              v-if="item.type == 'textarea'"
              type="textarea"
              v-model="ruleForm_model_7[item.props]"
              :rows="5"
              :placeholder="item.placeholder"
              style="resize: none"
              resize="none"
            ></el-input>
            <div v-if="item.type == 'customSelect'" style="position: relative">
              <el-input
                v-model="ruleForm_model_7[item.props]"
                :placeholder="item.placeholder"
                size="large"
                style="width: calc(100% - 20px)"
                @focus="changeValue(item, true, 'model_7_value')"
                @blur="changeValue(item, false, 'model_7_value')"
              />
              <div
                class="custom_select_box"
                v-if="model_7_value[item.props]"
              ></div>
            </div>
          </el-form-item>
          <el-form-item>
            <div
              style="display: flex; justify-content: center; padding-top: 100px"
            >
              <el-button type="primary" style="width: 162px">
                立即生成
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="model_7" v-if="type == 8">
      <div class="model_7_box">
        <div class="model_7_title">
          小红书文案

          <!-- <img src="../img/red.png" alt="" /> -->
        </div>
        <el-form
          :model="ruleForm_model_8"
          :rules="rules_model_8"
          ref="ruleForm"
          class="demo-ruleForm"
        >
          <div class="model_7_box_wrap">
            <el-form-item
              v-for="(item, index) in model_8_formList"
              :key="index"
              :label="item.label + ':'"
              :prop="item.props"
            >
              <el-input
                v-model="ruleForm_model_8[item.props]"
                v-if="item.type == 'input'"
                :placeholder="item.placeholder"
                size="large"
                style="width: calc(100% - 20px)"
              />
              <el-select
                v-model="ruleForm_model_8[item.props]"
                v-if="item.type == 'select'"
                :placeholder="item.placeholder"
                size="large"
                style="width: calc(100% - 20px)"
              />
              <el-input
                v-if="item.type == 'textarea'"
                type="textarea"
                v-model="ruleForm_model_8[item.props]"
                :rows="5"
                :placeholder="item.placeholder"
                style="width: calc(100% - 20px)"
                resize="none"
              ></el-input>
            </el-form-item>
            <template v-for="(item, index) in btnList_8">
              <el-form-item
                v-if="show_form_arr_8.includes(item.props)"
                :key="index"
                :label="item.label + ':'"
                :prop="item.props"
              >
                <el-select
                  v-if="item.type == 'select'"
                  v-model="ruleForm_model_8[item.props]"
                  style="width: calc(100% - 20px)"
                >
                  <el-option
                    v-for="citem in item.options"
                    :key="citem.value"
                    :label="citem.label"
                    :value="citem.value"
                  >
                    <span style="float: left">{{ citem.label }}</span>
                    <span
                      v-if="ruleForm_model_8[item.props] == citem.value"
                      style="float: right; font-size: 17px"
                    >
                      <i class="el-icon-check"></i>
                    </span>
                  </el-option>
                </el-select>
                <el-input
                  v-if="item.type == 'textarea'"
                  type="textarea"
                  v-model="ruleForm_model_8[item.props]"
                  :rows="5"
                  :placeholder="item.placeholder"
                  style="width: calc(100% - 20px)"
                  resize="none"
                ></el-input>
                <div
                  v-if="item.type == 'customSelect'"
                  style="position: relative"
                  class=""
                >
                  <el-input
                    v-model="ruleForm_model_8[item.props]"
                    :placeholder="item.placeholder"
                    size="large"
                    style="width: calc(100% - 20px)"
                    @focus="changeValue(item, true, 'model_8_value')"
                  />
                  <!-- @blur="changeValue(item, false, 'model_8_value')" -->
                  <div
                    class="custom_select_box"
                    v-if="model_8_value[item.props]"
                  >
                    <span
                      v-for="(sitem, sindex) in model_8_span[item.props]"
                      :key="sindex"
                      @click="setValue(sitem.name, item, 'ruleForm_model_8')"
                      :class="`${
                        ruleForm_model_8[item.props] == sitem.name
                          ? 'custom_select_box_active'
                          : ''
                      }`"
                    >
                      {{ sitem.name }}
                      <div
                        v-if="ruleForm_model_8[item.props] == sitem.name"
                        class="custom_select_box_active_icon"
                      >
                        <!-- <img src="../img/bg_active.png" alt="" /> -->
                        <i class="el-icon-check"></i>
                      </div>
                    </span>
                    <span class="custom_select_box_add">
                      <i class="el-icon-plus"></i>
                      自定义
                    </span>
                  </div>
                </div>
                <i
                  class="el-icon-remove remove-icon"
                  @click="removeArr(item)"
                ></i>
              </el-form-item>
            </template>
            <div class="model_btnList">
              <template v-for="(item, index) in btnList_8">
                <el-button
                  v-if="!show_form_arr_8.includes(item.props)"
                  :key="index"
                  icon="el-icon-plus"
                  size="small"
                  @click="show_form_arr_8.push(item.props)"
                >
                  {{ item.btnName }}
                </el-button>
              </template>
            </div>
          </div>
          <el-form-item>
            <div
              style="display: flex; justify-content: center; padding-top: 100px"
            >
              <el-button type="primary" style="width: 162px">
                立即生成
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="chat-box-container" v-if="type === 9">
      <div class="chat-box-container-box">
        <div class="chat-box-right">
          <div class="copywriting-page">
            <div class="copywriting-page__content-wrap">
              <div class="copywriting-page__content">
                <div class="copywriting-page-header">
                  <span>朋友圈文案</span>
                  <img
                    class="copywriting-page-header__redbook"
                    src="../img/pengyouquan.png"
                    alt=""
                  />
                </div>

                <div class="copywritin-content">
                  <div class="copywriting__model-select">
                    <div class="copywriting__model-select__label">
                      模型选择:
                    </div>
                    <div class="copywriting__model-select__options">
                      <div class="copywriting__model-select__option active">
                        <img
                          src="../img/gpt (1).png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span _nk="82Gd31">Chat4o-mini</span>
                      </div>
                      <div class="copywriting__model-select__option">
                        <img
                          src="../img/gpt.png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span>Chat4.0</span>
                        <img
                          src="../img/lock.png"
                          style="margin-left: 10px"
                          alt=""
                        />
                      </div>
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">
                        <span>*</span>
                        文案主题:
                      </div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="恋爱官宣/婚礼邀请函/喜得贵女/黄鹤楼打卡/祝福朋友结婚/骑行记录"
                      />
                    </div>
                  </div>

                  <div class="formItem-tone">
                    <div class="copywriting-select">
                      <div class="copywriting-select__content">
                        <div class="copywriting-select__label">语气:</div>
                        <div
                          class="copywriting-select__selected"
                          @click="
                            copywritingSelectShow = !copywritingSelectShow
                          "
                        >
                          <span
                            class="placeholder"
                            v-if="!copywritingSelectValue"
                          >
                            请选择语气
                          </span>
                          <span v-else>{{ copywritingSelectValue }}</span>
                          <i
                            :class="
                              copywritingSelectShow
                                ? 'el-icon-arrow-up'
                                : 'el-icon-arrow-down'
                            "
                          ></i>
                        </div>

                        <div
                          class="copywriting-select__options"
                          v-show="copywritingSelectShow"
                        >
                          <div class="copywriting-select__options-list">
                            <div
                              class="copywriting-select__options-item"
                              v-for="(item, index) in copywritingSelectList"
                              :key="index"
                              :class="[{ active: item.active }]"
                              @click="onClickCopywritingSelect(index)"
                            >
                              <span>{{ item.label }}</span>
                              <div
                                class="copywriting-select__options-item-status"
                                v-if="item.active"
                              >
                                <img src="../img/xuanze.png" alt="" />
                                <i class="el-icon-check"></i>
                              </div>
                            </div>
                            <div
                              class="copywriting-select__options-item custom"
                              @click="onOpenCustom"
                            >
                              <i class="el-icon-plus"></i>
                              <span>自定义</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="formItem-otherRequirements">
                    <div class="copywriting-textarea">
                      <div class="copywriting-textarea__label">其他要求:</div>
                      <div class="copywriting-textarea__content">
                        <el-input
                          type="textarea"
                          class="copywriting-textarea__textarea"
                          resize="none"
                          placeholder="要求对仗的形式/要求用古诗词/要求简单的一句话"
                        ></el-input>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="copywriting__submit">
                  <el-button type="primary">立即生成</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-box-container" v-if="type === 10">
      <div class="chat-box-container-box">
        <div class="chat-box-right">
          <div class="copywriting-page">
            <div class="copywriting-page__content-wrap">
              <div class="copywriting-page__content">
                <div class="copywriting-page-header">
                  <span>创意广告语</span>
                  <img
                    class="copywriting-page-header__redbook"
                    src="../img/guanggao.png"
                    alt=""
                  />
                </div>

                <div class="copywritin-content">
                  <div class="copywriting__model-select">
                    <div class="copywriting__model-select__label">
                      模型选择:
                    </div>
                    <div class="copywriting__model-select__options">
                      <div class="copywriting__model-select__option active">
                        <img
                          src="../img/gpt (1).png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span _nk="82Gd31">Chat4o-mini</span>
                      </div>
                      <div class="copywriting__model-select__option">
                        <img
                          src="../img/gpt.png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span>Chat4.0</span>
                        <img
                          src="../img/lock.png"
                          style="margin-left: 10px"
                          alt=""
                        />
                      </div>
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">
                        <span>*</span>
                        产品名称:
                      </div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="步步高点读机/脑白金/急支糖浆/葵花牌小儿肺热咳喘口服液"
                      />
                    </div>
                  </div>
                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">
                        体验产品/项目与特点:
                      </div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="请填写产品特点"
                      />
                    </div>
                  </div>

                  <div
                    class="formItem-tone"
                    v-if="copywritingMoreToolsValue.includes(2)"
                  >
                    <div class="copywriting-select">
                      <div class="copywriting-select__content">
                        <div class="copywriting-select__label">内容要求:</div>
                        <div
                          class="copywriting-select__selected"
                          @click="
                            copywritingContentSelectShow =
                              !copywritingContentSelectShow
                          "
                        >
                          <span
                            class="placeholder"
                            v-if="!copywritingContentSelectValue"
                          >
                            请选择语气
                          </span>
                          <span v-else>
                            {{ copywritingContentSelectValue }}
                          </span>
                          <i
                            :class="
                              copywritingContentSelectShow
                                ? 'el-icon-arrow-up'
                                : 'el-icon-arrow-down'
                            "
                          ></i>
                        </div>

                        <div
                          class="copywriting-line-select__options"
                          v-show="copywritingContentSelectShow"
                        >
                          <div
                            class="copywriting-line-select__options-item"
                            v-for="(
                              item, index
                            ) in copywritingContentSelectList"
                            :key="index"
                            :class="[{ active: item.active }]"
                            @click="onClickCopywritingContentSelect(index)"
                          >
                            <span>{{ item.label }}</span>
                            <i class="el-icon-check"></i>
                          </div>
                        </div>
                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="移除"
                          placement="top"
                        >
                          <div
                            class="copywriting-line-select__delete"
                            @click="onDeleteCopywritingMoreToolsValue(2)"
                          >
                            <i class="el-icon-error"></i>
                          </div>
                        </el-tooltip>
                      </div>
                    </div>
                  </div>

                  <div
                    class="formItem-tone"
                    v-if="copywritingMoreToolsValue.includes(1)"
                  >
                    <div class="copywriting-select">
                      <div class="copywriting-select__content">
                        <div class="copywriting-select__label">语气:</div>
                        <div
                          class="copywriting-select__selected"
                          @click="
                            copywritingSelectShow = !copywritingSelectShow
                          "
                        >
                          <span
                            class="placeholder"
                            v-if="!copywritingSelectValue"
                          >
                            请选择语气
                          </span>
                          <span v-else>{{ copywritingSelectValue }}</span>
                          <i
                            :class="
                              copywritingSelectShow
                                ? 'el-icon-arrow-up'
                                : 'el-icon-arrow-down'
                            "
                          ></i>
                        </div>

                        <div
                          class="copywriting-select__options"
                          v-show="copywritingSelectShow"
                        >
                          <div style="padding: 12px 12px 24px 14px">
                            <div class="copywriting-select__options-tools">
                              <i class="el-icon-setting"></i>
                            </div>
                            <div class="copywriting-select__options-list">
                              <div
                                class="copywriting-select__options-item"
                                v-for="(item, index) in copywritingSelectList"
                                :key="index"
                                :class="[{ active: item.active }]"
                                @click="onClickCopywritingSelect(index)"
                              >
                                <span>{{ item.label }}</span>
                                <div
                                  class="copywriting-select__options-item-status"
                                  v-if="item.active"
                                >
                                  <img src="../img/xuanze.png" alt="" />
                                  <i class="el-icon-check"></i>
                                </div>
                              </div>
                              <div
                                class="copywriting-select__options-item custom"
                                @click="onOpenCustom"
                              >
                                <i class="el-icon-plus"></i>
                                <span>自定义</span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="移除"
                          placement="top"
                        >
                          <div
                            class="copywriting-line-select__delete"
                            @click="onDeleteCopywritingMoreToolsValue(1)"
                          >
                            <i class="el-icon-error"></i>
                          </div>
                        </el-tooltip>
                      </div>
                    </div>
                  </div>

                  <div
                    class="formItem-otherRequirements"
                    v-if="copywritingMoreToolsValue.includes(3)"
                  >
                    <div class="copywriting-textarea">
                      <div class="copywriting-textarea__label">自定义要求:</div>
                      <div class="copywriting-textarea__content">
                        <el-input
                          type="textarea"
                          class="copywriting-textarea__textarea"
                          resize="none"
                          placeholder="例如：内容以问答的形式吸引读者"
                        ></el-input>
                      </div>
                      <el-tooltip
                        class="item"
                        effect="dark"
                        content="移除"
                        placement="top"
                      >
                        <div
                          class="copywriting-line-select__delete"
                          @click="onDeleteCopywritingMoreToolsValue(3)"
                        >
                          <i class="el-icon-error"></i>
                        </div>
                      </el-tooltip>
                    </div>
                  </div>

                  <div class="copywriting-add-more-tools">
                    <template v-for="(item, index) in copywritingMoreTools">
                      <div
                        class="copywriting-add-more-tools__item"
                        @click="copywritingMoreToolsValue.push(item.value)"
                        :style="item.style"
                        :key="index"
                        v-if="!copywritingMoreToolsValue.includes(item.value)"
                      >
                        <i class="el-icon-plus"></i>
                        <span>{{ item.label }}</span>
                      </div>
                    </template>
                  </div>
                </div>

                <div class="copywriting__submit">
                  <el-button type="primary">立即生成</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="page-container" v-if="type === 11">
      <div class="translate__container">
        <div class="translate__upload">
          <div class="translate__upload-title">上传或拖入要翻译的文档</div>
          <div class="translate__upload-btns">
            <el-button icon="el-icon-document">体验示例文档</el-button>
            <el-upload action="">
              <el-button type="primary" icon="el-icon-upload">
                上传文件
              </el-button>
            </el-upload>
          </div>
          <div class="translate__upload-description">
            <span>支持格式：</span>
            <div class="translate__upload-description-item">
              <img
                class="icon-pdf"
                src="../img/pdf.png"
                width="16"
                height="16"
                alt=""
              />
              <span>pdf（支持扫描文件）</span>
              <div class="translate__upload-description-item-tooltip">
                <img
                  class="icon-shoucang"
                  src="../img/shoucang.png"
                  width="16"
                  height="16"
                  alt=""
                />
                <span>推荐</span>
              </div>
            </div>
            <div class="translate__upload-description-item">
              <img
                class="icon-word"
                src="../img/word.png"
                width="16"
                height="16"
                alt=""
              />
              <span>word</span>
            </div>
            <div class="translate__upload-description-line"></div>
            <span>支持语言：中、英互译</span>
            <div class="translate__upload-description-line"></div>
            <span>支持大小: 15MB、500页以内</span>
          </div>
        </div>
      </div>

      <div class="translate__discription">
        <div class="translate__discription-list">
          <div
            class="translate__discription-item"
            v-for="(item, index) in discriptionList"
            :key="index"
            :class="[{ active: index === activeIndex }]"
            @click="onActiveIndex(index)"
          >
            <img
              :src="index === activeIndex ? item.iconCur : item.icon"
              class="icon"
              alt=""
            />
            <div class="translate__discription-item-container">
              <div class="translate__discription-item-title">
                {{ item.title }}
              </div>
              <div class="translate__discription-item-desc">{{ item.des }}</div>
            </div>
          </div>
        </div>
        <div
          class="translate__discription-img"
          :class="[activeContentImgClass]"
          :style="[{ backgroundImage: `url(${imageSrc})` }]"
        ></div>
      </div>
    </div>

    <div class="main-container" v-if="type === 12">
      <div class="box-left">
        <div class="left-history-x-general">
          <div
            class="left-history-x-general-btns"
            :class="[!drawShow ? 'hide' : 'show']"
          >
            <div class="ssb-eh__item">
              <img src="../img/jia.png" alt="" class="ssb-eh__item-icon" />
              <span class="ssb-eh__item-text">新建</span>
            </div>
            <div class="ssb-eh__item" @click="drawShow = false">
              <img src="../img/lishi.png" alt="" class="ssb-eh__item-icon" />
              <span class="ssb-eh__item-text">历史</span>
            </div>
          </div>
          <div
            class="left-history-x-general-listBox"
            :class="[drawShow ? 'hide' : 'show']"
          >
            <div
              class="history-x-listBoxs"
              :class="[drawShow ? 'hide' : 'show']"
            >
              <div class="lb-chat-left-btn">
                <i class="el-icon-plus"></i>
                新建论文摘要
              </div>
              <div class="history-com-x-general-list">
                <div class="ssb-hl-general-list-empty">
                  <div class="ssb-hl-general-list-empty__container">
                    <div class="ssb-hl-general-list-empty__img">
                      <img src="../img/empty.png" alt="" />
                    </div>
                    <div class="ssb-hl-general-list-empty__text">
                      暂时还没有历史记录哦，快去创建第一条吧~
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="history-x__close" @click="drawShow = true">
              <i class="el-icon-arrow-left"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="box-right">
        <div class="paper-abstract">
          <div class="paper-abstract-form-card">
            <div class="paper-abstract-form-card__header">
              <div class="paper-abstract-form-card__header-title">
                <img src="../img/lunwen.png" alt="" />
                论文摘要
              </div>
              <div class="paper-abstract-form-card__models">
                <div class="paper-abstract-form-card__model active">
                  <div class="paper-abstract-form-card__model-name">
                    Chat4o-mini
                  </div>
                  <img src="../img/lock.png" alt="" />
                </div>
                <div class="paper-abstract-form-card__model">
                  <div class="paper-abstract-form-card__model-name">
                    Chat4.0
                  </div>
                  <img src="../img/lock.png" alt="" />
                </div>
              </div>
            </div>

            <div class="paper-abstract-form-card__form">
              <div class="paper-abstract-form-card__form-item">
                <div class="paper-abstract-form-card__form-item-label">
                  生成模式
                </div>
                <div class="paper-abstract-form-card__generate-mode">
                  <el-radio-group v-model="radio">
                    <el-radio label="1" border>根据标题生成摘要</el-radio>
                    <el-radio label="2" border>根据文章提炼摘要</el-radio>
                  </el-radio-group>
                </div>
              </div>

              <div class="paper-abstract-article-input" v-if="radio == 2">
                <el-tabs v-model="activeName">
                  <el-tab-pane name="first">
                    <span
                      slot="label"
                      class="paper-abstract-article-input__tab-label"
                    >
                      <i class="el-icon-edit"></i>
                      粘贴论文内容
                    </span>
                    <div class="chat-paper-form-item">
                      <el-input
                        type="textarea"
                        resize="none"
                        style="height: 100%"
                        placeholder="在此处输入论文内容或切换上传文件"
                        maxlength="700"
                        show-word-limit
                      />
                    </div>
                  </el-tab-pane>
                  <el-tab-pane name="second">
                    <span
                      slot="label"
                      class="paper-abstract-article-input__tab-label"
                    >
                      <i class="el-icon-upload2"></i>
                      上传文件
                      <el-popover placement="bottom" trigger="click">
                        <div class="paper-abstract-article-input__vip-popup">
                          <i
                            class="el-icon-close paper-abstract-article-input__close-btn"
                          ></i>
                          <span class="paper-abstract-article-input__vip-txt1">
                            “上传文件”需解锁“文件解读”权限
                          </span>
                          <span class="paper-abstract-article-input__vip-txt2">
                            “文件解读”为
                            <span style="color: rgb(234, 159, 92)">会员</span>
                            专享
                          </span>
                          <button
                            class="paper-abstract-article-input__vip-button"
                          >
                            <img
                              src="../img/xiazai.png"
                              width="14"
                              height="14"
                            />
                            开通会员
                          </button>
                        </div>
                        <img
                          slot="reference"
                          src="../img/vip.png"
                          alt=""
                          style="line-height: 30px; display: block"
                        />
                      </el-popover>
                    </span>
                    <el-upload
                      drag
                      action=""
                      class="paper-abstract-upload"
                      multiple
                    >
                      <div
                        class="paper-abstract-upload__placeholder"
                        _nk="aY/A31"
                      >
                        <img
                          src="../img/upload-img.png"
                          height="60"
                          width="60"
                        />
                        <span style="color: rgb(140, 140, 140)">
                          点击上传或拖拽文件至此处，文件小于15M
                        </span>
                        <span style="color: rgb(140, 140, 140)">
                          此功能将会消耗1次“文件解读”次数
                        </span>
                      </div>
                    </el-upload>
                  </el-tab-pane>
                </el-tabs>
              </div>
              <el-form
                label-position="top"
                label-width="80px"
                :model="formLabelAlign"
              >
                <el-form-item label="论文标题" v-if="radio == 1">
                  <el-input
                    v-model="formLabelAlign.name"
                    placeholder="在此处输入论文标题"
                  ></el-input>
                </el-form-item>

                <el-form-item
                  label="生成要求（选填）
  "
                >
                  <el-input
                    type="textarea"
                    v-model="formLabelAlign.type"
                    resize="none"
                    show-word-limit
                    maxlength="100"
                    placeholder="在此处输入摘要生成要求"
                  ></el-input>
                </el-form-item>
              </el-form>
            </div>
            <div class="paper-abstract-form-card__footer">
              <div class="paper-abstract-form-card__wordcount-select">
                <span style="color: rgb(114, 114, 114); font-size: 13px">
                  致谢字数：
                </span>
                <el-select
                  v-model="value"
                  size="small"
                  placeholder="请选择"
                  style="width: 120px"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
              <button class="paper-abstract-form-card__submit-btn">
                立即生成
              </button>
            </div>
          </div>
          <div class="paper-abstract-result-card">
            <div class="paper-abstract-result-card__head">
              <div class="paper-abstract-result-card__head-title">摘要内容</div>
            </div>
            <div class="paper-abstract-result-card__body">
              <div class="paper-abstract-result-card__result-empty">
                <img src="../img/shoushi.png" alt="" />
                在左侧输入内容并提交，将为你生成摘要内容
              </div>
              <el-input
                readonly
                class="paper-abstract-result-card__result"
                type="textarea"
                resize="none"
              ></el-input>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-container" v-if="type === 13">
      <div class="box-left">
        <div class="left-history-x-general">
          <div
            class="left-history-x-general-btns"
            :class="[!drawShow ? 'hide' : 'show']"
          >
            <div class="ssb-eh__item">
              <img src="../img/jia.png" alt="" class="ssb-eh__item-icon" />
              <span class="ssb-eh__item-text">新建</span>
            </div>
            <div class="ssb-eh__item" @click="drawShow = false">
              <img src="../img/lishi.png" alt="" class="ssb-eh__item-icon" />
              <span class="ssb-eh__item-text">历史</span>
            </div>
          </div>
          <div
            class="left-history-x-general-listBox"
            :class="[drawShow ? 'hide' : 'show']"
          >
            <div
              class="history-x-listBoxs"
              :class="[drawShow ? 'hide' : 'show']"
            >
              <div class="lb-chat-left-btn">
                <i class="el-icon-plus"></i>
                新建论文摘要
              </div>
              <div class="history-com-x-general-list">
                <div class="ssb-hl-general-list-empty">
                  <div class="ssb-hl-general-list-empty__container">
                    <div class="ssb-hl-general-list-empty__img">
                      <img src="../img/empty.png" alt="" />
                    </div>
                    <div class="ssb-hl-general-list-empty__text">
                      暂时还没有历史记录哦，快去创建第一条吧~
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="history-x__close" @click="drawShow = true">
              <i class="el-icon-arrow-left"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="box-right">
        <div class="paper-thanks">
          <div class="paper-thanks-form-card">
            <div class="paper-thanks-form-card__header">
              <div class="paper-thanks-form-card__header-title">
                <img src="../img/lunwen.png" alt="" />
                论文致谢
              </div>
              <div class="paper-thanks-form-card__models">
                <div class="paper-thanks-form-card__model active">
                  <div class="paper-thanks-form-card__model-name">
                    Chat4o-mini
                  </div>
                  <img src="../img/lock.png" alt="" />
                </div>
                <div class="paper-thanks-form-card__model">
                  <div class="paper-thanks-form-card__model-name">Chat4.0</div>
                  <img src="../img/lock.png" alt="" />
                </div>
              </div>
            </div>

            <div class="paper-thanks-form-card__form">
              <el-form
                label-position="top"
                label-width="80px"
                :model="formLabelAlign"
              >
                <el-form-item label="论文标题：">
                  <el-input
                    v-model="formLabelAlign.name"
                    placeholder="在此处输入论文标题"
                  ></el-input>
                </el-form-item>
                <el-form-item label="导师姓名：">
                  <el-input
                    v-model="formLabelAlign.region"
                    placeholder="在此处输入导师姓名"
                  ></el-input>
                </el-form-item>
                <el-form-item label="研究机构或实验室：">
                  <el-input
                    v-model="formLabelAlign.type"
                    placeholder="在此处输入研究机构或实验室名称"
                  ></el-input>
                </el-form-item>
                <el-form-item label="项目或基金：">
                  <el-input
                    v-model="formLabelAlign.type"
                    placeholder="在此处输入具体的项目或基金名称"
                  ></el-input>
                </el-form-item>
                <el-form-item label="其他补充：">
                  <el-input
                    type="textarea"
                    v-model="formLabelAlign.type"
                    resize="none"
                    show-word-limit
                    maxlength="500"
                    placeholder="在此处输入其他补充内容或生成要求"
                  ></el-input>
                </el-form-item>
              </el-form>
            </div>
            <div class="paper-thanks-form-card__footer">
              <div class="paper-thanks-form-card__wordcount-select">
                <span style="color: rgb(114, 114, 114); font-size: 13px">
                  致谢字数：
                </span>
                <el-select
                  v-model="value"
                  size="small"
                  placeholder="请选择"
                  style="width: 120px"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
              <button class="paper-thanks-form-card__submit-btn">
                生成致谢
              </button>
            </div>
          </div>
          <div class="paper-thanks-result-card">
            <div class="paper-thanks-result-card__head">
              <div class="paper-thanks-result-card__head-title">
                论文致谢结果
              </div>
            </div>
            <div class="paper-thanks-result-card__body">
              <div class="paper-thanks-result-card__result-empty">
                <img src="../img/shoushi.png" alt="" />
                在左侧输入内容并提交，将为你生成论文致谢
              </div>
              <el-input
                readonly
                class="paper-thanks-result-card__result"
                type="textarea"
                resize="none"
              ></el-input>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-box-container" v-if="type === 14">
      <div class="chat-box-container-box">
        <div class="chat-box-right">
          <div class="copywriting-page">
            <div class="copywriting-page__content-wrap">
              <div class="copywriting-page__content">
                <div class="copywriting-page-header">
                  <span>招聘文案</span>
                  <img
                    class="copywriting-page-header__redbook"
                    src="../img/zhaopin.png"
                    alt=""
                  />
                </div>

                <div class="copywritin-content">
                  <div class="copywriting__model-select">
                    <div class="copywriting__model-select__label">
                      模型选择:
                    </div>
                    <div class="copywriting__model-select__options">
                      <div class="copywriting__model-select__option active">
                        <img
                          src="../img/gpt (1).png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span _nk="82Gd31">Chat4o-mini</span>
                      </div>
                      <div class="copywriting__model-select__option">
                        <img
                          src="../img/gpt.png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span>Chat4.0</span>
                        <img
                          src="../img/lock.png"
                          style="margin-left: 10px"
                          alt=""
                        />
                      </div>
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">
                        <span>*</span>
                        岗位名称:
                      </div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="产品经理/高级前端工程师/UI设计师/大数据工程师"
                      />
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">岗位要求:</div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="1.三年以上软件产品经理岗位工作经验，有0-1成功案例者优先
  2.具备从产品需求分析、设计、研发、上线到运营全过程的产品经验"
                        type="textarea"
                        resize="none"
                      />
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">联系方式:</div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="添加您的邮件地址或手机号"
                      />
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">岗位职责:</div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="1.根据公司产品战略，进行行业调研和竞品分析，制定产品开发及迭代需求；
  2.能独立设计产品原型，制定0-1产品规划，高效输出PRD交付给开发和测试团队；"
                        type="textarea"
                        resize="none"
                      />
                    </div>
                  </div>

                  <template v-for="item in formFieldList">
                    <div class="formItem-title" :key="item.field">
                      <div class="copywriting-input">
                        <div class="copywriting-input__label">
                          {{ item.label }}:
                        </div>

                        <template v-if="item.type === 'input'">
                          <el-input
                            type="text"
                            class="copywriting-input__input"
                            :placeholder="item.placeholder"
                          ></el-input>
                        </template>
                        <template v-if="item.type === 'select'">
                          <div
                            class="copywriting-select__selected"
                            @click="
                              copywritingSelectShow2 = !copywritingSelectShow2
                            "
                          >
                            <span
                              class="placeholder"
                              v-if="!copywritingSelectValue2"
                            >
                              请选择语气
                            </span>
                            <span v-else>{{ copywritingSelectValue2 }}</span>
                            <i
                              :class="
                                copywritingSelectShow2
                                  ? 'el-icon-arrow-up'
                                  : 'el-icon-arrow-down'
                              "
                            ></i>
                          </div>

                          <div
                            class="copywriting-select__options"
                            v-show="copywritingSelectShow2"
                          >
                            <div style="padding: 12px 12px 24px 14px">
                              <div class="copywriting-select__options-tools">
                                <i class="el-icon-setting"></i>
                              </div>
                              <div class="copywriting-select__options-list">
                                <div
                                  class="copywriting-select__options-item"
                                  v-for="(
                                    item, index
                                  ) in copywritingSelectList2"
                                  :key="index"
                                  :class="[{ active: item.active }]"
                                  @click="onClickCopywritingSelect2(index)"
                                >
                                  <span>{{ item.label }}</span>
                                  <div
                                    class="copywriting-select__options-item-status"
                                    v-if="item.active"
                                  >
                                    <img src="../img/xuanze.png" alt="" />
                                    <i class="el-icon-check"></i>
                                  </div>
                                </div>
                                <div
                                  class="copywriting-select__options-item custom"
                                  @click="onOpenCustom"
                                >
                                  <i class="el-icon-plus"></i>
                                  <span>自定义</span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </template>

                        <template v-if="item.type === 'textarea'">
                          <el-input
                            type="textarea"
                            class="copywriting-textarea__textarea"
                            resize="none"
                            :placeholder="item.placeholder"
                          ></el-input>
                        </template>

                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="移除"
                          placement="top"
                        >
                          <div
                            class="copywriting-line-select__delete"
                            @click="onDeleteCopywritingMoreToolsValue2(item)"
                          >
                            <i class="el-icon-error"></i>
                          </div>
                        </el-tooltip>
                      </div>
                    </div>
                  </template>

                  <div class="copywriting-add-more-tools">
                    <template v-for="(item, index) in copywritingMoreTools2">
                      <div
                        class="copywriting-add-more-tools__item"
                        :style="item.style"
                        :key="index"
                        @click="onAddCopywritingMoreToolsValue2(item)"
                      >
                        <i class="el-icon-plus"></i>
                        <span>添加{{ item.label }}</span>
                      </div>
                    </template>
                  </div>
                </div>

                <div class="copywriting__submit">
                  <el-button type="primary">立即生成</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-box-container" v-if="type === 15">
      <div class="chat-box-container-box">
        <div class="chat-box-right">
          <div class="copywriting-page">
            <div class="copywriting-page__content-wrap">
              <div class="copywriting-page__content">
                <div class="copywriting-page-header">
                  <span>探店文案</span>
                  <img
                    class="copywriting-page-header__redbook"
                    src="../img/tandian.png"
                    alt=""
                  />
                </div>

                <div class="copywritin-content">
                  <div class="copywriting__model-select">
                    <div class="copywriting__model-select__label">
                      模型选择:
                    </div>
                    <div class="copywriting__model-select__options">
                      <div class="copywriting__model-select__option active">
                        <img
                          src="../img/gpt (1).png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span _nk="82Gd31">Chat4o-mini</span>
                      </div>
                      <div class="copywriting__model-select__option">
                        <img
                          src="../img/gpt.png"
                          style="margin-right: 10px"
                          alt=""
                        />
                        <span>Chat4.0</span>
                        <img
                          src="../img/lock.png"
                          style="margin-left: 10px"
                          alt=""
                        />
                      </div>
                    </div>
                  </div>

                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">
                        <span>*</span>
                        店铺名称:
                      </div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="小民大排档/椰岛造型/中田健身工作室"
                      />
                    </div>
                  </div>
                  <div class="formItem-tone">
                    <div class="copywriting-select">
                      <div class="copywriting-select__content">
                        <div class="copywriting-select__label">店铺类型:</div>
                        <div
                          class="copywriting-select__selected"
                          @click="shopTypeSelectShow = !shopTypeSelectShow"
                        >
                          <span class="placeholder" v-if="!shopTypeSelectValue">
                            请选择店铺类型
                          </span>
                          <span v-else>{{ shopTypeSelectValue }}</span>
                          <i
                            :class="
                              shopTypeSelectShow
                                ? 'el-icon-arrow-up'
                                : 'el-icon-arrow-down'
                            "
                          ></i>
                        </div>

                        <div
                          class="copywriting-select__options"
                          v-show="shopTypeSelectShow"
                        >
                          <div style="padding: 12px 12px 24px 14px">
                            <div class="copywriting-select__options-tools">
                              <i class="el-icon-setting"></i>
                            </div>
                            <div class="copywriting-select__options-list">
                              <div
                                class="copywriting-select__options-item"
                                v-for="(item, index) in shopTypeSelectList"
                                :key="index"
                                :class="[{ active: item.active }]"
                                @click="onClickCopywritingSelect(index)"
                              >
                                <span>{{ item.label }}</span>
                                <div
                                  class="copywriting-select__options-item-status"
                                  v-if="item.active"
                                >
                                  <img src="../img/xuanze.png" alt="" />
                                  <i class="el-icon-check"></i>
                                </div>
                              </div>
                              <div
                                class="copywriting-select__options-item custom"
                                @click="onOpenCustom"
                              >
                                <i class="el-icon-plus"></i>
                                <span>自定义</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="formItem-title">
                    <div class="copywriting-input">
                      <div class="copywriting-input__label">
                        体验产品/项目与特点:
                      </div>

                      <el-input
                        class="copywriting-input__input"
                        placeholder="香辣牛蛙：肉很嫩"
                        type="textarea"
                        resize="none"
                      />
                    </div>
                  </div>

                  <div
                    class="formItem-tone"
                    v-if="copywritingMoreToolsValue.includes(2)"
                  >
                    <div class="copywriting-select">
                      <div class="copywriting-select__content">
                        <div class="copywriting-select__label">稿件内容:</div>
                        <div
                          class="copywriting-select__selected"
                          @click="
                            copywritingContentSelectShow =
                              !copywritingContentSelectShow
                          "
                        >
                          <span
                            class="placeholder"
                            v-if="!copywritingContentSelectValue"
                          >
                            请选择语气
                          </span>
                          <span v-else>
                            {{ copywritingContentSelectValue }}
                          </span>
                          <i
                            :class="
                              copywritingContentSelectShow
                                ? 'el-icon-arrow-up'
                                : 'el-icon-arrow-down'
                            "
                          ></i>
                        </div>

                        <div
                          class="copywriting-line-select__options"
                          v-show="copywritingContentSelectShow"
                        >
                          <div
                            class="copywriting-line-select__options-item"
                            v-for="(
                              item, index
                            ) in copywritingContentSelectList"
                            :key="index"
                            :class="[{ active: item.active }]"
                            @click="onClickCopywritingContentSelect(index)"
                          >
                            <span>{{ item.label }}</span>
                            <i class="el-icon-check"></i>
                          </div>
                        </div>
                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="移除"
                          placement="top"
                        >
                          <div
                            class="copywriting-line-select__delete"
                            @click="onDeleteCopywritingMoreToolsValue(2)"
                          >
                            <i class="el-icon-error"></i>
                          </div>
                        </el-tooltip>
                      </div>
                    </div>
                  </div>

                  <div
                    class="formItem-tone"
                    v-if="copywritingMoreToolsValue.includes(1)"
                  >
                    <div class="copywriting-select">
                      <div class="copywriting-select__content">
                        <div class="copywriting-select__label">语气:</div>
                        <div
                          class="copywriting-select__selected"
                          @click="
                            copywritingSelectShow = !copywritingSelectShow
                          "
                        >
                          <span
                            class="placeholder"
                            v-if="!copywritingSelectValue"
                          >
                            请选择语气
                          </span>
                          <span v-else>{{ copywritingSelectValue }}</span>
                          <i
                            :class="
                              copywritingSelectShow
                                ? 'el-icon-arrow-up'
                                : 'el-icon-arrow-down'
                            "
                          ></i>
                        </div>

                        <div
                          class="copywriting-select__options"
                          v-show="copywritingSelectShow"
                        >
                          <div style="padding: 12px 12px 24px 14px">
                            <div class="copywriting-select__options-tools">
                              <i class="el-icon-setting"></i>
                            </div>
                            <div class="copywriting-select__options-list">
                              <div
                                class="copywriting-select__options-item"
                                v-for="(item, index) in copywritingSelectList"
                                :key="index"
                                :class="[{ active: item.active }]"
                                @click="onClickCopywritingSelect(index)"
                              >
                                <span>{{ item.label }}</span>
                                <div
                                  class="copywriting-select__options-item-status"
                                  v-if="item.active"
                                >
                                  <img src="../img/xuanze.png" alt="" />
                                  <i class="el-icon-check"></i>
                                </div>
                              </div>
                              <div
                                class="copywriting-select__options-item custom"
                                @click="onOpenCustom"
                              >
                                <i class="el-icon-plus"></i>
                                <span>自定义</span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="移除"
                          placement="top"
                        >
                          <div
                            class="copywriting-line-select__delete"
                            @click="onDeleteCopywritingMoreToolsValue(1)"
                          >
                            <i class="el-icon-error"></i>
                          </div>
                        </el-tooltip>
                      </div>
                    </div>
                  </div>

                  <div
                    class="formItem-otherRequirements"
                    v-if="copywritingMoreToolsValue.includes(3)"
                  >
                    <div class="copywriting-textarea">
                      <div class="copywriting-textarea__label">自定义要求:</div>
                      <div class="copywriting-textarea__content">
                        <el-input
                          type="textarea"
                          class="copywriting-textarea__textarea"
                          resize="none"
                          placeholder="例如：内容以问答的形式吸引读者"
                        ></el-input>
                      </div>
                      <el-tooltip
                        class="item"
                        effect="dark"
                        content="移除"
                        placement="top"
                      >
                        <div
                          class="copywriting-line-select__delete"
                          @click="onDeleteCopywritingMoreToolsValue(3)"
                        >
                          <i class="el-icon-error"></i>
                        </div>
                      </el-tooltip>
                    </div>
                  </div>

                  <div class="copywriting-add-more-tools">
                    <template v-for="(item, index) in copywritingMoreTools">
                      <div
                        class="copywriting-add-more-tools__item"
                        @click="copywritingMoreToolsValue.push(item.value)"
                        :style="item.style"
                        :key="index"
                        v-if="!copywritingMoreToolsValue.includes(item.value)"
                      >
                        <i class="el-icon-plus"></i>
                        <span>{{ item.label }}</span>
                      </div>
                    </template>
                  </div>
                </div>

                <div class="copywriting__submit">
                  <el-button type="primary">立即生成</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-container" v-if="type === 16">
      <div class="box-left">
        <div class="left-history-x-general">
          <div
            class="left-history-x-general-btns"
            :class="[!drawShow ? 'hide' : 'show']"
          >
            <div class="ssb-eh__item">
              <img src="../img/jia.png" alt="" class="ssb-eh__item-icon" />
              <span class="ssb-eh__item-text">新建</span>
            </div>
            <div class="ssb-eh__item" @click="drawShow = false">
              <img src="../img/lishi.png" alt="" class="ssb-eh__item-icon" />
              <span class="ssb-eh__item-text">历史</span>
            </div>
          </div>
          <div
            class="left-history-x-general-listBox"
            :class="[drawShow ? 'hide' : 'show']"
          >
            <div
              class="history-x-listBoxs"
              :class="[drawShow ? 'hide' : 'show']"
            >
              <div class="lb-chat-left-btn">
                <i class="el-icon-plus"></i>
                新建论文摘要
              </div>
              <div class="history-com-x-general-list">
                <div class="ssb-hl-general-list-empty">
                  <div class="ssb-hl-general-list-empty__container">
                    <div class="ssb-hl-general-list-empty__img">
                      <img src="../img/empty.png" alt="" />
                    </div>
                    <div class="ssb-hl-general-list-empty__text">
                      暂时还没有历史记录哦，快去创建第一条吧~
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="history-x__close" @click="drawShow = true">
              <i class="el-icon-arrow-left"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="box-right">
        <div class="translate_box">
          <div class="translate_box-content">
            <div class="chat-translate-header">
              <div
                class="chat-translate-header__tab-item with-text"
                :class="[{ active: !modelShow }]"
                @click="modelShow = false"
              >
                <i class="el-icon-s-unfold chat-translate-header__tab-icon"></i>
                <span class="chat-translate-header__tab-text">文字</span>
              </div>
              <div
                class="chat-translate-header__tab-item with-image"
                :class="[{ active: modelShow }]"
                @click="modelShow = true"
              >
                <i
                  class="el-icon-picture-outline chat-translate-header__tab-icon"
                ></i>
                <span class="chat-translate-header__tab-text">图片</span>
              </div>
            </div>

            <!-- ! -->

            <div
              class="translate_box__container"
              :class="[{ hide: modelShow }]"
            >
              <div class="translate_box_item translate_box_content">
                <div class="translate_content_box">
                  <div class="translate_content_header">
                    <div class="select_language_tab">
                      <div class="select_language_box">
                        <div
                          class="select_language_btn"
                          @click="onClickLanguagStartShow()"
                        >
                          <span class="select_language_text">
                            自动检测源语言(英语)
                          </span>
                          <i
                            class="select_language_arrow"
                            :class="[
                              !languagStartShow
                                ? 'el-icon-arrow-down'
                                : 'el-icon-arrow-up',
                            ]"
                          ></i>
                        </div>

                        <div
                          class="select_language_section"
                          v-if="languagStartShow"
                        >
                          <div class="search">
                            <el-input
                              placeholder="请输入内容"
                              prefix-icon="el-icon-search"
                            ></el-input>
                          </div>
                          <div class="language_list">
                            <div
                              class="language_item"
                              v-for="(item, index) in languageList"
                              :key="index"
                            >
                              {{ item }}
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="el-icon-right translagte_content_tab"></div>
                      <div class="select_language_box">
                        <div
                          class="select_language_btn"
                          @click="onClickLanguagEndShow()"
                        >
                          <span class="select_language_text">中文</span>
                          <i
                            class="select_language_arrow"
                            :class="[
                              !languagEndShow
                                ? 'el-icon-arrow-down'
                                : 'el-icon-arrow-up',
                            ]"
                          ></i>
                        </div>
                        <div
                          class="select_language_section"
                          v-if="languagEndShow"
                        >
                          <div class="search">
                            <el-input
                              placeholder="请输入内容"
                              prefix-icon="el-icon-search
  "
                            ></el-input>
                          </div>
                          <div class="language_list">
                            <div
                              class="language_item"
                              v-for="(item, index) in languageList.slice(1)"
                              :key="index"
                            >
                              {{ item }}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="translate_content_body">
                    <el-input
                      placeholder="输入或粘贴要翻译的内容，当日剩余体验次数(3次)"
                      type="textarea"
                      resize="none"
                    ></el-input>
                  </div>

                  <div class="translate_content_footer">
                    <div class="nums">0/2000</div>
                    <div class="btns">
                      <el-tooltip
                        class="item"
                        effect="dark"
                        content="清空内容"
                        placement="top"
                      >
                        <div class="btns_item">
                          <i
                            class="translagte_content_btn el-icon-delete-solid"
                          ></i>
                        </div>
                      </el-tooltip>
                    </div>

                    <el-button type="primary" disabled size="mini">
                      立即翻译
                    </el-button>
                  </div>
                </div>
              </div>

              <!-- ! -->
              <div class="translate_box_item translate_box_result">
                <div class="translate_result_box">
                  <div class="translate_result_header">
                    <div class="translate_result_title">
                      <div class="title_icon"><i></i></div>
                      <div class="title_text">翻译结果</div>
                      <div class="tab_power">
                        <div class="tab-power_box">
                          <div class="tab-power-item active">
                            <span>Chat4o-mini</span>
                          </div>
                          <div class="tab-power-item">
                            <span>Chat4.0</span>
                            <img
                              src="../img/lock.png"
                              class="lock_icon"
                              alt=""
                            />
                          </div>
                          <div class="tab-power-item">
                            <span>Claude 3.5 Sonnet</span>
                            <img
                              src="../img/lock.png"
                              class="lock_icon"
                              alt=""
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="translate_result_body">
                    <el-input
                      class=""
                      type="textarea"
                      placeholder="在左侧输入要翻译的内容后，将自动为你翻译"
                      readonly
                      resize="none"
                    ></el-input>

                    <img src="../img/shoushi.png" alt="" />

                    <div class="footerInfo">
                      <div class="nums">0</div>
                      <div class="btn">
                        <i class="el-icon-copy-document"></i>
                      </div>
                    </div>
                  </div>

                  <div class="translate_result_footer">
                    <div class="translate_result_title">
                      <div class="title_icon">
                        <i></i>
                      </div>
                      <div class="text">定制翻译</div>
                    </div>

                    <div class="translate_quest_buttons">
                      <div class="quest_buttons_box">
                        <div
                          class="quest_list"
                          v-for="(item, index) in questList"
                          :key="index"
                        >
                          <div
                            class="quest_buttons_item"
                            @click="onClickQuestList(item)"
                          >
                            <span>{{ item.label }}</span>
                            <i class="el-icon-arrow-down"></i>
                            <div
                              class="quest_button_select_content"
                              v-if="item.showMore"
                            >
                              <div class="title">{{ item.label }}:</div>
                              <el-tooltip
                                effect="dark"
                                :content="children"
                                placement="left"
                                v-for="(children, index) in item.children"
                                :key="index"
                              >
                                <div class="select_item">
                                  <span>{{ children }}</span>
                                </div>
                              </el-tooltip>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="input_area">
                      <el-input
                        placeholder="添加个性化翻译要求，或补充背景信息"
                        type="textarea"
                        resize="none"
                      ></el-input>
                      <div class="button_publish">
                        <i class="el-icon-position"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- ! -->

            <div
              class="translate_box__container translate-img-box"
              :class="[{ hide: !modelShow }]"
            >
              <div class="translate_box_item translate_box_content">
                <div class="translate-img-box__language-box">
                  <div class="select_language_tab">
                    <div class="select_language_box">
                      <div
                        class="select_language_btn"
                        @click="languagStartShow = !languagStartShow"
                      >
                        <span class="select_language_text">
                          自动检测源语言(英语)
                        </span>
                        <i
                          class="select_language_arrow"
                          :class="[
                            !languagStartShow
                              ? 'el-icon-arrow-down'
                              : 'el-icon-arrow-up',
                          ]"
                        ></i>
                      </div>

                      <div
                        class="select_language_section"
                        v-if="languagStartShow"
                      >
                        <div class="search">
                          <el-input
                            placeholder="请输入内容"
                            prefix-icon="el-icon-search
  "
                          ></el-input>
                        </div>
                        <div class="language_list">
                          <div
                            class="language_item"
                            v-for="(item, index) in languageList"
                            :key="index"
                          >
                            {{ item }}
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="el-icon-right translagte_content_tab"></div>
                    <div class="select_language_box">
                      <div
                        class="select_language_btn"
                        @click="languagEndShow = !languagEndShow"
                      >
                        <span class="select_language_text">中文</span>
                        <i
                          class="select_language_arrow"
                          :class="[
                            !languagEndShow
                              ? 'el-icon-arrow-down'
                              : 'el-icon-arrow-up',
                          ]"
                        ></i>
                      </div>
                      <div
                        class="select_language_section"
                        v-if="languagEndShow"
                      >
                        <div class="search">
                          <el-input
                            placeholder="请输入内容"
                            prefix-icon="el-icon-search
  "
                          ></el-input>
                        </div>
                        <div class="language_list">
                          <div
                            class="language_item"
                            v-for="(item, index) in languageList.slice(1)"
                            :key="index"
                          >
                            {{ item }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="translate-img-box__img-box">
                  <div class="translate-img-upload">
                    <div class="translate-img-upload__init">
                      <img
                        src="../img/upload-img.png"
                        class="translate-img-upload__init__upload-icon"
                        alt=""
                      />
                      <div class="translate-img-upload__init__upload-title">
                        拖拽/点击上传图片
                      </div>
                      <div class="translate-img-upload__init__upload-tips">
                        图片尺寸：15px*15px ~ 2048px*2048px，限4MB以内
                      </div>
                      <div class="translate-img-upload__init__upload-tips des">
                        （纯色图片翻译效果最佳哦~）
                      </div>
                    </div>
                  </div>
                </div>

                <div class="translate-img-box__left-bottom-box">
                  <el-button
                    type="primary"
                    class="translate-img-box__start-btn"
                    disabled
                    size="mini"
                  >
                    立即翻译
                  </el-button>
                </div>
              </div>

              <div class="translate_box_item translate_box_result">
                <div class="translate-img-box__guide">
                  <div class="translate-img-box__guide__title">
                    <div class="translate-img-box__guide__title-icon">
                      <i></i>
                    </div>
                    <div class="translate-img-box__guide__title-text">
                      翻译结果
                    </div>
                  </div>
                  <div class="translate-img-box__guide__tips">
                    结合 AI 人工智能，翻译书面文档、路标、菜单、手册等场景使用
                    NO .1👍
                  </div>
                  <div class="translate-img-box__guide__tips">
                    上传图片直接翻译，准确率高，维持原有排版格式！✌
                  </div>
                  <div class="translate-img-box__guide__tips-item">
                    <div class="translate-img-box__guide__tips-title">
                      翻译前：
                    </div>
                    <img
                      src="../img/img-guide-left.png"
                      class="translate-img-box__guide__tips-img"
                    />
                  </div>
                  <div class="translate-img-box__guide__tips-item">
                    <div class="translate-img-box__guide__tips-title">
                      翻译后：
                    </div>
                    <img
                      src="../img/img-guide-right.png"
                      class="translate-img-box__guide__tips-img"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-button
      v-if="type !== 0"
      type="primary"
      icon="el-icon-close"
      circle
      style="
        height: 40px;
        width: 40px;
        background-color: #6965ea;
        border-color: #6965ea;
        position: absolute;
        top: 10px;
        right: 20px;
      "
      @click="type = 0"
    ></el-button>
  </div>
</template>

<script>
  const languageList = [
    '自动检测源语言',
    '爱沙尼亚语',
    '保加利亚语',
    '波兰语',
    '丹麦语',
    '俄语',
    '法语',
    '捷克语',
    '拉脱维亚语',
    '立陶宛语',
    '罗马尼亚语',
    '葡萄牙语',
    '日语',
    '瑞典语',
    '书面挪威语',
    '芬兰语',
    '韩语',
    '荷兰语',
    '斯洛伐克语',
    '斯洛文尼亚语',
    '土耳其语',
    '乌克兰语',
    '西班牙语',
    '希腊语',
    '匈牙利语',
    '意大利语',
    '印尼语',
    '英语',
    '中文',
    '德语',
  ]
  // import Logo from "@/assets/logo.png";
  import axios from 'axios'
  export default {
    data() {
      return {
        type: 0,
        // imgUrl: Logo,
        searchStatus: false,
        searchValue: '',
        navIndex: 0,
        list: [
          '相向而行的力量',
          '小城市何以撬动文旅大市场',
          '谢娜曾反对张杰去歌手',
          '男生地铁上被女子偷拍追上硬刚',
          '西安一副镇长半夜叫别人妻子唱歌',
          '菏泽郭有才为什么就火了',
        ],
        textareaHeight: 24,
        newList: [
          {
            icon: 'el-icon-s-comment',
            nav: '对话专区',
            navTips: '汇聚全球尖端对话模型，懂你所想',
            children: [
              { icon: 'el-icon-setting', name: '定制模型' },
              { icon: 'el-icon-user', name: '角色扮演' },
              { icon: 'el-icon-document-checked', name: '文件解读' },
              { icon: 'el-icon-data-analysis', name: '统计图' },
              { icon: 'el-icon-picture-outline', name: '图片解读' },
              { icon: 'el-icon-data-line', name: '流程图' },
              { icon: 'el-icon-data-board', name: '思维导图' },
              { icon: 'el-icon-discount', name: '个人模板' },
            ],
          },
          {
            icon: 'el-icon-picture',
            nav: '绘画专区',
            navTips: '多元绘画模型，释放创作潜能',
            children: [
              { icon: 'el-icon-picture-outline', name: '以图生图' },
              { icon: 'el-icon-picture-outline', name: '图片转指令' },
              { icon: 'el-icon-picture-outline', name: '多图融合' },
              { icon: 'el-icon-picture-outline', name: '动漫模式' },
              { icon: 'el-icon-picture-outline', name: '图片动漫化' },
              { icon: 'el-icon-picture-outline', name: '摄影模式' },
              { icon: 'el-icon-picture-outline', name: '写真相机' },
              { icon: 'el-icon-picture-outline', name: 'LOGO' },
            ],
          },
          {
            icon: 'el-icon-s-custom',
            nav: '学术专区',
            navTips: '20+学术工具套件，搞学术更轻松',
            children: [
              { icon: 'el-icon-picture-outline', name: '网盘知识库' },
              { icon: 'el-icon-picture-outline', name: '文献翻译', id: 6 },
              { icon: 'el-icon-picture-outline', name: 'AI帮读' },
              { icon: 'el-icon-picture-outline', name: '论文润色' },
              { icon: 'el-icon-picture-outline', name: '学术审稿' },
              { icon: 'el-icon-picture-outline', name: 'PDF转WORD' },
            ],
          },
          {
            icon: 'el-icon-notebook-1',
            nav: '论文写作',
            navTips: '使用大模型生成或修改论文',
            children: [
              { icon: 'el-icon-picture-outline', name: '论文改写', id: 1 },
              { icon: 'el-icon-picture-outline', name: '论文致谢', id: 2 },
              { icon: 'el-icon-picture-outline', name: '论文提纲', id: 3 },
              { icon: 'el-icon-picture-outline', name: '论文摘要', id: 4 },
              { icon: 'el-icon-picture-outline', name: '论文续写', id: 5 },
            ],
          },
          {
            icon: 'el-icon-s-goods',
            nav: '职场专区',
            navTips: '掌握职场高效力，一键提升工作效率',
            children: [
              { icon: 'el-icon-picture-outline', name: 'AI翻译', id: 11 },
              { icon: 'el-icon-picture-outline', name: 'PPT生成' },
              { icon: 'el-icon-picture-outline', name: '小红书文案', id: 8 },
              { icon: 'el-icon-picture-outline', name: '招聘文案', id: 14 },
              { icon: 'el-icon-picture-outline', name: '数据分析' },
              { icon: 'el-icon-picture-outline', name: '财报解读' },
              { icon: 'el-icon-picture-outline', name: '直播带货' },
              { icon: 'el-icon-picture-outline', name: '朋友圈文案', id: 9 },
              { icon: 'el-icon-picture-outline', name: '探店文案', id: 15 },
              { icon: 'el-icon-picture-outline', name: 'AI读书' },
              { icon: 'el-icon-picture-outline', name: '工作总结' },
              { icon: 'el-icon-picture-outline', name: '创意广告语', id: 10 },
              { icon: 'el-icon-picture-outline', name: '活动策划' },
            ],
          },
        ],
        // 显示论文改写页面状态
        lunwengaixie: true,
        textarea1: '',
        textarea2: '',
        textarea3: '',
        tagList: [
          '段落间过渡不够流畅',
          '表达不够清晰，要更简洁明了',
          '使用更专业的术语',
        ],
        // 论文致谢相关字段
        lunwenzhixie: false,
        ruleForm: {},
        sizeNumber: '300',
        rules: {
          title: [
            { required: true, message: '请输入论文标题', trigger: 'blur' },
          ],
        },
        // 论文提纲相关字段
        lunwentigang: false,
        ruleFormTiGang: {},
        rulesTiGang: {
          title: [
            { required: true, message: '请输入论文标题', trigger: 'blur' },
          ],
        },
        // 论文摘要
        lunwenzhaiyao: false,
        rulesZhaiyao: {},
        ruleFormZhaiyao: {},
        lunwenxuxie: false,
        tagList1: ['保持续写内容语气一致性', '对观点深入解释和讨论'],
        textarea4: '',
        textarea5: '',
        sizeNumber1: '300',
        textarea6: '',
        textarea7: '',
        textarea8: '',

        textarea5_1: '',
        textarea5_2: '',

        model_6_list: [
          {
            title: '排版格式还原：精确保留文档原貌',
            content: '确保每一段、每个图表都与原文档完美匹配，无缝保留结构细节',
            img: '../img/1.png',
          },
          {
            title: '领先大模型：革新学术翻译',
            content:
              '基于全球尖端大模型，确保翻译结果既忠实原意，也符合专业标准',
            img: '../img/2.png',
          },
          {
            title: '术语精准还原：纯正学术表达',
            content:
              '覆盖逾800+学科、6000万+术语词汇库，赋予译文深度的学术支撑',
            img: '../img/3.png',
          },
          {
            title: '语境适应：理解复杂文本',
            content:
              '不再是单词直译，能结合上下文，确保内容符合其特定领域的真实含义',
            img: '../img/4.png',
          },
        ],
        model_6_list_active: 0,
        ruleForm_model_7: {},
        rules_model_7: {
          title: [{ required: true, message: '请输入' }],
        },
        model_7_formList: [
          {
            label: '文案主题',
            props: 'title',
            type: 'input',
            placeholder:
              '恋爱官宣/婚礼邀请函/喜得贵女/黄鹤楼打卡/祝福朋友结婚/骑行记录',
          },
          { label: '语气', props: 'yuqi', type: 'customSelect' },
          { label: '其他要求', props: 'remark', type: 'textarea' },
        ],
        ruleForm_model_8: {},
        rules_model_8: {
          title: [{ required: true, message: '请输入' }],
        },
        model_8_formList: [
          {
            label: '主题',
            props: 'title',
            type: 'input',
            placeholder:
              '理肤泉面膜种草/武汉旅游攻略/迪士尼打卡文案/iPhone 15测评',
          },
          {
            label: '作者身份',
            props: 'yuqi',
            type: 'select',
            placeholder: '请选择作者身份',
          },
          {
            label: '仿写',
            props: 'remark',
            type: 'textarea',
            placeholder: '粘贴您想仿写的内容',
          },
        ],
        show_form_arr_8: [],
        btnList_8: [
          {
            btnName: '添加语气',
            props: 'value1',
            label: '语气',
            type: 'customSelect',
          },
          {
            btnName: '添加关键词',
            props: 'value2',
            label: '关键词',
            type: 'textarea',
          },
          {
            btnName: '添加文案目的',
            props: 'value3',
            label: '文案目的',
            type: 'select',
            options: [
              { label: '引发讨论', value: 0 },
              { label: '寻求共鸣', value: 1 },
            ],
          },
          {
            btnName: '指定文案内容',
            props: 'value4',
            label: '文案内容',
            type: 'select',
            options: [],
          },
          {
            btnName: '设置分段要求',
            props: 'value5',
            label: '分段要求',
            type: 'select',
            options: [],
          },
          {
            btnName: '自定义要求',
            props: 'value6',
            label: '自定义要求',
            type: 'textarea',
          },
        ],
        model_8_value: {},
        model_7_value: {},
        model_8_span: {
          value1: [{ name: '幽默' }, { name: '真诚' }, { name: '搞笑' }],
        },

        copywritingSelectList: [
          { label: '正式', active: false },
          { label: '幽默', active: false },
          { label: '真诚', active: false },
          { label: '搞笑', active: false },
        ],
        copywritingSelectShow: false,
        copywritingSelectValue: '',

        copywritingContentSelectList: [
          {
            label: '要注重押韵',
            active: false,
            value: 1,
          },
          {
            label: '更利于传播',
            active: false,
            value: 1,
          },
          {
            label: '更利于记忆',
            value: 1,
            active: false,
          },
        ],
        copywritingContentSelectShow: false,
        copywritingContentSelectValue: '',

        copywritingMoreTools: [
          {
            label: '添加语气',
            value: 1,
            style: { 'border-color': 'rgb(136, 204, 160)' },
          },
          {
            label: '指定内容要求',
            value: 2,
            style: { 'border-color': 'rgb(216, 209, 124)' },
          },
          {
            label: '自定义要求',
            value: 3,
            style: { 'border-color': 'rgb(136, 208, 242)' },
          },
        ],

        copywritingMoreToolsValue: [],

        discriptionList: [
          {
            title: '排版格式还原：精确保留文档原貌',
            des: '确保每一段、每个图表都与原文档完美匹配，无缝保留结构细节',
            contentImgClass: 'composition',
            icon: require('../img/paiban.png'),
            iconCur: require('../img/paiban-cur.png'),
          },
          {
            title: '领先大模型：革新学术翻译',
            des: '基于全球尖端大模型，确保翻译结果既忠实原意，也符合专业标准',
            contentImgClass: 'leading_model',
            icon: require('../img/paiban.png'),
            iconCur: require('../img/paiban-cur.png'),
          },
          {
            title: '术语精准还原：纯正学术表达',
            des: '覆盖逾800+学科、6000万+术语词汇库，赋予译文深度的学术支撑',
            contentImgClass: 'science',
            icon: require('../img/paiban.png'),
            iconCur: require('../img/paiban-cur.png'),
          },
          {
            title: '语境适应：理解复杂文本',
            des: '不再是单词直译，能结合上下文，确保内容符合其特定领域的真实含义',
            contentImgClass: 'language',
            icon: require('../img/paiban.png'),
            iconCur: require('../img/paiban-cur.png'),
          },
        ],
        activeIndex: 0,
        activeContentImgClass: 'composition',

        labelPosition: 'right',
        formLabelAlign: {
          name: '',
          region: '',
          type: '',
        },
        activeName: 'first',
        radio: '1',
        options: [
          {
            value: '选项1',
            label: '300字以内',
          },
          {
            value: '选项2',
            label: '300-500字',
          },
          {
            value: '选项3',
            label: '500字以上',
          },
        ],
        value: '',

        drawShow: true,

        formFieldList: [],

        copywritingSelectList2: [
          { label: '共情与呼吁', active: false },
          { label: '幽默', active: false },
          { label: '真诚', active: false },
          { label: '搞笑', active: false },
        ],

        copywritingSelectShow2: false,
        copywritingSelectValue2: '',

        copywritingMoreTools2: [
          {
            label: '公司名称',
            field: 'field1',
            style: { 'border-color': 'rgb(136, 204, 160)' },
          },
          {
            label: '公司福利',
            field: 'field2',
            style: { 'border-color': 'rgb(216, 209, 124)' },
          },
          {
            label: '加分项',
            field: 'field3',
            style: { 'border-color': 'rgb(136, 208, 242)' },
          },
          {
            label: '附加信息',
            field: 'field4',
            style: { 'border-color': 'rgb(136, 204, 160)' },
          },
          {
            label: '语气',
            field: 'field5',
            style: { 'border-color': 'rgb(136, 204, 160)' },
          },
          {
            label: '自定义要求',
            field: 'field6',
            style: { 'border-color': 'rgb(136, 204, 160)' },
          },
        ],

        copywritingMoreToolsValue2: [],

        shopTypeSelectList: [
          { label: '美食', active: false },
          { label: '美容', active: false },
          { label: '美发', active: false },
          { label: '美甲', active: false },
          { label: '酒店', active: false },
          { label: '健身房', active: false },
          { label: '电影院', active: false },
          { label: '游乐场', active: false },
          { label: '景点', active: false },
          { label: '服装店', active: false },
        ],
        shopTypeSelectShow: false,
        shopTypeSelectValue: '',

        modelShow: false,
        languageList,

        languagStartShow: false,
        languagEndShow: false,

        questList: [
          {
            label: '文本类型',
            showMore: false,
            children: [
              '通用',
              '学术论文',
              '生活',
              '邮件',
              '营销文案',
              '申请书',
              '小说',
              '文件文档',
              '诗歌古文',
            ],
          },
          { label: '长度', children: ['简洁一些', '长一些'], showMore: false },
          {
            label: '内容结构',
            showMore: false,
            children: [
              '合并短句',
              '拆分长句',
              '使用主动语态',
              '使用被动语态',
              '添加连接词',
              '多样化用词',
              '剔除复杂词',
              '使用专业词汇',
              '补充细节',
              '结构简明',
            ],
          },
          {
            label: '语言风格',
            showMore: false,
            children: [
              '地道口语',
              '书面表达',
              '幽默诙谐',
              '生动有趣',
              '专业正式',
              '现代风格',
              '传统风格',
              '情感强烈',
              '客观中立',
              '简单易懂',
              '引入修辞',
            ],
          },
        ],
      }
    },
    computed: {
      imageSrc() {
        // 动态引入图片
        return require(`../img/${this.activeContentImgClass}.png`)
      },
    },
    mounted() {
      // this.$refs.autoresizing.style.height = "24px";
    },
    methods: {
      setValue(value, data, name) {
        this[name] = {
          ...this[name],
          [data.props]: value,
        }
      },
      changeValue(data, value, name) {
        this[name] = {
          ...this[name],
          [data.props]: value,
        }
      },
      removeArr(data) {
        this.show_form_arr_8 = this.show_form_arr_8.filter(
          (item) => item !== data.props
        )
      },
      // 提交改写
      submitEidt() {
        axios
          .post('https://hit-mitlab.cn:7860/generate_doc', {
            prompt: '请针对以下内容进行润色修改：' + this.textarea1,
            temperature: 1.0,
            max_tokens: 2048,
          })
          .then((res) => {
            this.textarea3 = res.data.text
          })
      },
      submit5() {
        axios
          .post('https://hit-mitlab.cn:7860/generate_doc', {
            prompt: '请针对以下内容进行续写：' + this.textarea5_1,
            temperature: 1.0,
            max_tokens: 2048,
          })
          .then((res) => {
            this.textarea5_1 = this.textarea5_1 + res.data.text
          })
      },
      submitEidtXuXie() {},
      // 论文致谢
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!')
          } else {
            return false
          }
        })
      },
      // 论文提纲
      submitFormTiGang(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!')
          } else {
            return false
          }
        })
      },
      submitFormZhaiyao(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!')
          } else {
            return false
          }
        })
      },
      handleSelect(item) {
        console.log(item)
      },
      resize(event) {
        const textarea = event.target
        textarea.style.height = '24px'
        textarea.style.height = textarea.scrollHeight + 'px'
      },

      onClickCopywritingSelect(index) {
        this.copywritingSelectValue = this.copywritingSelectList[index].label

        this.copywritingSelectList.map((item) => (item.active = false))
        this.copywritingSelectList[index].active = true
        this.copywritingSelectShow = false
      },

      onOpenCustom() {
        this.$prompt('', '自定义语气', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        })
          .then(({ value }) => {
            this.copywritingSelectList.unshift({ label: value, active: true })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '取消输入',
            })
          })
      },
      onDeleteCopywritingMoreToolsValue(value) {
        this.copywritingMoreToolsValue.splice(
          this.copywritingMoreToolsValue.indexOf(value),
          1
        )
      },
      onActiveIndex(index) {
        this.activeIndex = index
        this.activeContentImgClass = this.discriptionList[index].contentImgClass
      },

      onAddCopywritingMoreToolsValue2(item) {
        switch (item.field) {
          case 'field1':
            this.formFieldList.push({
              label: '公司名称',
              field: 'field1',
              type: 'input',

              style: { 'border-color': 'rgb(136, 204, 160)' },
              placeholder: '武汉xxxx科技有限公司',
            })
            break
          case 'field2':
            this.formFieldList.push({
              label: '公司福利',
              field: 'field2',
              type: 'input',
              style: { 'border-color': 'rgb(216, 209, 124)' },
              placeholder: '1.早九晚六，周末双休',
            })
            break
          case 'field3':
            this.formFieldList.push({
              label: '加分项',
              field: 'field3',
              type: 'input',
              style: { 'border-color': 'rgb(136, 208, 242)' },
              placeholder: '1.双一流院校毕业生优先',
            })
            break
          case 'field4':
            this.formFieldList.push({
              label: '附加信息',
              field: 'field4',
              type: 'input',
              style: { 'border-color': 'rgb(136, 204, 160)' },
              placeholder:
                '1.公司位置：xx市xx区xx路xx号，可乘坐x号线；2.本岗位属于急聘岗位，要求尽快到岗；',
            })
            break
          case 'field5':
            this.formFieldList.push({
              label: '语气',
              field: 'field5',
              type: 'select',
              style: { 'border-color': 'rgb(136, 204, 160)' },
            })
            break
          case 'field6':
            this.formFieldList.push({
              label: '自定义要求',
              field: 'field6',
              type: 'textarea',
              style: { 'border-color': 'rgb(136, 204, 160)' },
              placeholder: '例如：内容以问答的形式吸引读者',
            })
        }
        this.copywritingMoreTools = this.copywritingMoreTools.filter(
          (o) => o.field !== item.field
        )
      },
      onClickCopywritingSelect2(index) {
        this.copywritingSelectValue2 = this.copywritingSelectList2[index].label

        this.copywritingSelectList2.map((item) => (item.active = false))
        this.copywritingSelectList2[index].active = true
        this.copywritingSelectShow2 = false
      },

      onDeleteCopywritingMoreToolsValue2(item) {
        this.copywritingMoreTools2.push(item)
        this.formFieldList = this.formFieldList.filter(
          (o) => o.field !== item.field
        )
      },

      onClickQuestList(item) {
        item.showMore = !item.showMore
      },

      onClickLanguagStartShow() {
        this.languagEndShow = false
        this.languagStartShow = !this.languagStartShow
      },
      onClickLanguagEndShow() {
        this.languagStartShow = false
        this.languagEndShow = !this.languagEndShow
      },
    },
  }
</script>

<style scoped>
  .home_title {
    position: absolute;
    top: 10%;
    font-size: 16px;
  }
  .model_7_box_wrap {
    max-height: calc(100vh - 300px);
    overflow-y: scroll;
    scrollbar-width: none;
    scrollbar-height: none;
  }
  .custom_select_box {
    position: absolute;
    z-index: 1;
    top: 13px;
    width: calc(100% - 20px);
    padding: 12px 12px 24px 14px;
    -webkit-transform: translateY(100%);
    transform: translateY(100%);
    background: #ffffff;
    box-shadow: 0 8px 12px 1px #00000014;
    border-radius: 8px;
    border: 1px solid #e2e2e2;
    -webkit-animation: toShow 0.2s ease-in;
    animation: toShow 0.2s ease-in;
    box-sizing: border-box;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
  .custom_select_box span.custom_select_box_active {
    border-color: #6663e5;
    background: rgba(105, 101, 234, 0.12);
  }
  .custom_select_box_active_icon {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 16px;
    height: 14px;
  }
  .custom_select_box_active_icon i {
    position: absolute;
    bottom: 0;
    right: 0;
    color: #fff;
  }
  .custom_select_box_active_icon img {
    display: block;
    width: 100%;
    height: 100%;
  }
  .custom_select_box span {
    position: relative;
    min-width: 88px;
    padding: 0 10px;
    margin: 4px;
    height: 36px;
    text-align: center;
    line-height: 36px;
    background: #f1f1f1;
    border-radius: 6px;
    font-size: 13px;
    color: #434343;
    border: 1px solid transparent;
    cursor: pointer;
    overflow: hidden;
  }
  .custom_select_box .custom_select_box_add {
    border: 1px dashed #d9d9d9;
    background-color: #fff;
  }
  .custom_select_box .custom_select_box_add i {
    margin-right: 10px;
  }
  .model_btnList {
    padding-top: 20px;
  }
  .model_btnList .el-button {
    background: transparent;
    color: #141414 !important;
    border-color: #9992f7;
    border-radius: 6px;
    height: 35px;
  }
  .model_btnList .el-button:nth-child(2n) {
    border-color: rgb(216, 209, 124);
  }
  .model_btnList .el-button:nth-child(3n) {
    border-color: rgb(130, 208, 242);
  }
  .model_7 {
    background: #f7f7f8;
    min-height: calc(100vh - 60px);
    box-sizing: border-box;
    position: relative;
  }
  .model_7 .model_7_box {
    width: 738px;
    margin: 0 auto;
  }
  .model_7 .model_7_box .model_7_title {
    padding: 27px 0 19px;
    display: flex;
    align-items: center;
    font-size: 20px;
    color: #000;
    border-bottom: 1px solid #dedede;
    font-weight: 550;
  }
  .model_7 .model_7_box .model_7_title img {
    width: 46px;
    height: 46px;
    margin-left: 10px;
  }
  .model_7 .el-form-item__content {
    position: relative;
  }
  .model_7 .el-form-item__content .remove-icon {
    position: absolute;
    right: 0px;
    top: 56px;
    cursor: pointer;
    color: rgb(255, 69, 69);
  }
  ::v-deep .el-button--primary {
    background: #9992f7;
    border-color: #9992f7;
  }
  ::v-deep .model_7 .el-form .el-form-item {
    margin: 40px 0 0;
  }
  ::v-deep .model_7 .el-form label {
    width: 100%;
    text-align: left;
    font-size: 13px;
    color: #000;
  }
  ::v-deep .model_7 .el-form .el-form-item__content {
    margin-left: 0;
  }
  ::v-deep .model_7 .el-form .el-input .el-input__inner {
    border-radius: 8px;
    height: 46px;
  }
  ::v-deep .model_7 .el-form .el-textarea__inner {
    border-radius: 8px;
  }
  ::v-deep .model_7 .el-form .el-textarea__inner:hover {
    border-color: #9992f7;
  }
  ::v-deep .model_7 .el-form .el-textarea__inner:focus {
    border-color: #9992f7;
  }
  ::v-deep .model_7 .el-form .el-input:hover {
    border-color: #9992f7;
  }
  ::v-deep .model_7 .el-form .el-input.is-active .el-input__inner,
  .el-input__inner:focus {
    border-color: #9992f7;
  }
  .model_6 {
    display: flex;
    justify-content: center;
    background: linear-gradient(180deg, #e6effc 0%, #ffffff 100%);
    padding-top: 60px;
    min-height: calc(100vh - 60px);
  }
  .model_6_box {
    width: 1060px;
  }
  .model_6_upload {
    width: 100%;
    padding: 20px;
    margin: 0 auto;
    background: #fefefe;
    box-shadow: 0 10px 16px 1px #2d2f400a;
    border-radius: 16px;
    height: 300px;
  }
  .model_6_introduce {
    width: 100%;
    display: flex;
    margin: 50px auto;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 16px;
    border: 1px solid #eef3fa;
    height: 370px;
  }
  .model_6_introduce_right {
    flex: 1;
    background-repeat: no-repeat;
    background-position: center center;
    background-size: contain;
  }
  .model_6_introduce_right.model_6_introduce_right_size {
    background-size: initial;
  }
  .model_6_introduce_left {
    width: 372px;
    display: flex;
    flex-direction: column;
  }
  .model_6_introduce_left_item {
    flex: 1;
    position: relative;
    cursor: pointer;
    border-radius: 10px;
    padding: 16px 16px 16px 56px;
    overflow: hidden;
    box-sizing: border-box;
  }
  .model_6_introduce_left_item_title {
    font-size: 14px;
    line-height: 18px;
    margin-bottom: 6px;
  }
  .model_6_introduce_left_item_title i {
    position: absolute;
    top: 18px;
    transform: translate(-20px, -0);
  }
  .model_6_introduce_left_item_content {
    font-size: 12px;
    color: #595959;
  }
  .model_6_introduce_left_item_active {
    box-shadow: 0 8px 20px 1px #2d2f4014;
  }
  .model_6_introduce_left_item_active .model_6_introduce_left_item_title {
    color: #6965ea;
  }
  .model_6_introduce_left_item_active
    .model_6_introduce_left_item_title::before {
    content: '';
    display: block;
    position: absolute;
    left: 0;
    top: 0;
    width: 3px;
    height: 100%;
    background: #aba8fe;
  }
  .model_6_upload_box {
    box-sizing: border-box;
    background: #fafafc;
    border-radius: 4px;
    border: 1px dashed #d4d4d4;
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
  }
  ::v-deep .el-upload {
    width: 100%;
    height: 100%;
  }

  ::v-deep .el-upload .el-upload-dragger {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  ::v-deep .el-upload .el-upload-dragger .el-icon-upload {
    font-size: 20px;
    line-height: 20px;
    margin: 0;
    color: #fff;
  }
  ::v-deep .el-upload .el-upload-dragger .el-button--primary {
    display: flex;
    align-items: center;
    margin: 24px auto 40px;
    background: #6965ea;
    border-color: #6965ea;
  }
  .appContent {
    /* width: calc(100vw - 20px); */
    width: 100%;
    /* height: calc(100vh - 60px); */
    /* background: url('../../../assets/indexBg.png'); */
    background-repeat: no-repeat;
    background-size: cover;
    position: relative;
    background-color: #f5f7ff;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .textareaBox {
    border-radius: 12px;
    display: flex;
    gap: 10px;
    /* min-height: 50px; */
    padding: 10px 20px;
    position: relative;
    width: 80%;
    max-width: 980px;
    background: #fff;
    margin-top: 200px;
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: flex-start;
    gap: 10px;
  }

  .textareaBoxMenu {
    height: 30px;
    width: 30px;
    border-radius: 100%;
    background: #ebedf3;
    text-align: center;
    line-height: 30px;
    cursor: pointer;
    user-select: none;
  }

  .textareaContainer {
    flex: 1 1 auto;
  }

  .textarea {
    width: 100%;
    resize: none;
    min-height: 24px;
    max-height: 360px;
    outline: none;
    border: none;
    padding: 0;
    line-height: 24px;
    color: rgba(0, 0, 0, 0.88);
    font-size: 14px;
    background-color: #ffffff;
  }

  .textareaBoxSend {
    color: #fff;
    width: 30px;
    height: 30px;
    background: #6965ea;
    border-radius: 8px;
    /* display: flex;
  align-items: center;
  justify-content: center; */
    margin-right: -8px;
    cursor: pointer;
    text-align: center;
    line-height: 30px;
  }

  .boxContent {
    width: calc(100% - 120px);
    max-width: 1200px;
    margin: 30px auto 20px;
    padding-bottom: 50px;
  }

  .boxContentItem {
    border-bottom: 1px solid #ebeefa;
    padding-bottom: 32px;
    margin-top: 32px;
  }

  .itemTitleBox {
    width: 100%;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .itemTitleBoxBg {
    background: linear-gradient(
      90deg,
      rgba(86, 114, 247, 0.1) 0%,
      rgba(86, 114, 247, 0.02) 100%
    );
    height: 38px;
    border-radius: 16px 16px 6px 6px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding-left: 12px;
    height: 100%;
    line-height: 38px;
  }

  .boxContentItem:nth-child(2) .itemTitleBoxBg {
    background: linear-gradient(
      90deg,
      rgba(119, 99, 235, 0.1) 0%,
      rgba(119, 99, 235, 0.02) 100%
    );
  }

  .boxContentItem:nth-child(3) .itemTitleBoxBg {
    background: linear-gradient(
      90deg,
      rgba(230, 171, 5, 0.1) 0%,
      rgba(230, 171, 5, 0.02) 100%
    );
  }

  .boxContentItem:nth-child(4) .itemTitleBoxBg {
    background: linear-gradient(
      90deg,
      rgba(57, 179, 93, 0.1) 0%,
      rgba(57, 179, 93, 0.02) 100%
    );
  }

  .boxContentItem:nth-child(5) .itemTitleBoxBg {
    background: linear-gradient(
      90deg,
      rgba(63, 178, 224, 0.1) 0%,
      rgba(63, 178, 224, 0.02) 100%
    );
  }

  .itemTitleBoxIcon {
    font-size: 16px;
    margin-right: 8px;
    display: flex;
    align-items: center;
    color: #908dea;
  }

  .itemTitleBoxTitle {
    font-size: 15px;
    color: #323a4c;
    margin-right: 8px;
    font-weight: 700;
  }

  .itemTitleBoxLine {
    font-size: 12px;
    color: #7b7b7b;
    margin-right: 8px;
  }

  .itemTitleBoxBlurb {
    font-size: 12px;
    color: #7b7b7b;
    margin-right: 37px;
  }

  .itemTitleBoxMore {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    font-size: 12px;
    color: #6965ea;
    cursor: pointer;
  }

  .itemTitleBoxRight {
    margin-left: 8px;
  }

  .itemListBox {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: wrap;
  }

  .contentNavItem {
    width: 150px;
    height: 46px;
    background: linear-gradient(133deg, #fafbff 0%, #ffffff 28%, #ffffff 100%);
    box-shadow: 0 2px 4px 1px #4b4b590a;
    border-radius: 10px;
    border: 1px solid #ffffff;
    display: flex;
    justify-content: center;
    /* align-items: center; */
    cursor: pointer;
    margin: 20px 16px 0 0;
    position: relative;
    transition: border 0.2s;
  }

  .contentNavItem:hover {
    border: 1px solid #908dea;
  }

  .contentNavItemIcon {
    font-size: 18px;
    margin-right: 8px;
    color: #908dea;
    height: 46px;
    line-height: 46px;
  }

  .contentNavItemTitle {
    max-width: 100px;
    height: 18px;
    font-size: 13px;
    color: #141414;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    height: 46px;
    line-height: 46px;
  }

  .header {
    width: calc(100% - 20px);
    padding: 10px;
    display: flex;
    justify-content: space-between;
  }
  .bottom {
    width: 100vw;
    position: fixed;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 40px;
    line-height: 40px;
    color: #bbb;
    font-size: 12px;
  }
  .bottom span {
    margin-right: 14px;
    cursor: pointer;
  }
  .bottom span:hover {
    color: #222;
  }

  .navBox {
    display: flex;
    padding-left: 10px;
    padding-top: 5px;
  }
  .navItem {
    margin-right: 24px;
    color: #222;
    font-size: 13px;
    cursor: pointer;
  }

  .navItem:hover {
    color: #315efb;
  }

  .content {
    width: 655px;
    margin: 0 auto;
    text-align: center;
    padding-top: 150px;
  }

  .searchBox {
    display: flex;
    margin-top: 20px;
  }

  .searchBox .el-input__inner {
    border-radius: 4px 0 0 4px !important;
  }

  .searchBox .el-button {
    border-radius: 0 4px 4px 0 !important;
  }

  .listBox {
    display: flex;
    flex-wrap: wrap;
    margin: 0 auto;
    width: 655px;
    /* padding-top: 5px; */
  }

  .listBoxBtn {
    display: flex;
    flex-wrap: wrap;
    margin: 0 auto;
    width: 655px;
    padding-top: 30px;
    justify-content: space-between;
  }

  .listBoxBtnLeft {
    font-size: 15px;
    color: #222222;
    font-weight: bold;
    cursor: pointer;
  }

  .listBoxBtnRight {
    font-size: 14px;
    color: #626675;
    cursor: pointer;
  }

  .listBoxBtnLeft:hover {
    color: #315efb;
  }

  .listBoxBtnRight:hover {
    color: #315efb;
  }

  .listItem {
    width: 50%;
    height: 36px;
    line-height: 36px;
    font-size: 16px;
    color: #222;
    cursor: pointer;
  }

  .listItem .num {
    margin-right: 15px;
    font-size: 18px;
    color: #9195a3;
  }

  .listItem:hover > .title {
    color: #315efb;
    text-decoration: underline;
  }

  .app {
    width: calc(100vw - 20px);
    height: calc(100vh - 60px);
    display: flex;
    /* justify-content: center; */
    flex-direction: column;
    align-items: center;
    /* padding-top: 100px; */
  }
  .headerBoxs {
    width: calc(100vw - 20px);
    height: 65px;
    /* box-shadow: 0 2px 10px 0 rgba(0,0,0,.1); */
    display: flex;
    justify-content: center;
  }
  .headers {
    width: 1200px;
    padding: 10px 0;
    display: flex;
    position: fixed;
    top: 55px;
    padding-top: 15px;
    background-color: #f6f8f9;
    z-index: 99;
    /* justify-content: space-between; */
  }

  .contents {
    width: 655px;
  }

  .searchBoxs {
    display: flex;
  }

  .searchBoxs .el-input__inner {
    border-radius: 4px 0 0 4px !important;
  }

  .searchBoxs .el-button {
    border-radius: 0 4px 4px 0 !important;
  }
  .logins {
    flex: 1;
    text-align: right;
  }
  .navBoxs {
    display: flex;
    margin-bottom: 10px;
  }
  .navItems {
    color: #626675;
    margin-right: 27px;
    line-height: 28px;
    text-align: left;
    margin-top: 4px;
    font-size: 14px;
    cursor: pointer;
  }
  .navItemActiveds {
    border-bottom: 2px solid #38f;
  }
  .navItems i {
    font-size: 13px;
    margin-right: 2px;
  }
  .results {
    width: 1200px;
    max-height: calc(100vh - 110px);
    /* padding-top: 10px; */
    display: flex;
    justify-content: space-between;
    position: relative;
  }
  .lefts {
    width: 600px;
  }
  .leftItems {
    margin-bottom: 20px;
  }
  .leftTitles {
    color: #2440b3;
    text-decoration: underline;
    font-size: 18px;
    cursor: pointer;
  }
  .leftTitles span {
    color: #f73131;
  }
  .leftContents {
    font-size: 13px;
    color: #333333;
    line-height: 21px;
    margin-top: 4px;
  }
  .leftContents span {
    color: #f73131;
  }
  .leftAuthors {
    font-size: 13px;
    color: #626675;
    margin-top: 2px;
  }
  .rights {
    width: 370px;
    position: absolute;
    top: 98px;
    right: 0px;
  }
  .listItems {
    width: 370px;
    height: 32px;
    line-height: 32px;
    font-size: 13px;
    color: #2440b3;
    cursor: pointer;
  }

  .listItems .nums {
    margin-right: 10px;
    font-size: 15px;
    color: #9195a3;
  }

  .listItems:hover > .titles {
    color: #315efb;
    text-decoration: underline;
  }

  .listBoxBtns {
    display: flex;
    flex-wrap: wrap;
    width: 300px;
    padding-top: 30px;
    justify-content: space-between;
  }

  .listBoxBtnLefts {
    font-size: 15px;
    color: #222222;
    font-weight: bold;
    cursor: pointer;
  }

  .listBoxBtnRights {
    font-size: 14px;
    color: #626675;
    cursor: pointer;
  }

  .listBoxBtnLefts:hover {
    color: #315efb;
  }

  .listBoxBtnRights:hover {
    color: #315efb;
  }

  .logo {
    width: 40px;
    height: 40px;
    margin-right: 10px;
  }
  .lunwengaixie_box {
    width: calc(100%);
    height: calc(100vh - 60px);
    min-width: 1170px;
    min-height: 466px;
    background: #f2f2f5;
    padding: 30px;
    display: flex;
    gap: 30px;
    box-sizing: border-box;
  }
  .lunwenzhixie_box {
    width: calc(100%);
    height: calc(100vh - 60px);
    min-width: 1170px;
    min-height: 466px;
    background: #f2f2f5;
    padding: 30px;
    display: flex;
    gap: 30px;
    box-sizing: border-box;
    overflow-y: scroll;
  }
  .lunwenxuxie_box {
    width: calc(100%);
    height: calc(100vh - 60px);
    min-width: 1170px;
    min-height: 466px;
    background: #f2f2f5;
    padding: 30px;
    display: flex;
    gap: 30px;
    box-sizing: border-box;
    overflow-y: scroll;
  }
  .lunwenxuxie_content {
    width: calc(100%);
    min-width: 1000px;
    padding: 30px;
    border-radius: 12px;
    background: white;
    display: flex;
    justify-content: center;
  }
  .lunwenxuxie_content_box {
    width: 774px;
  }
  .lunwengaixie_box_left {
    min-width: 706px;
    flex: 1;
    height: calc(100%);
    padding: 30px;
    border-radius: 12px;
    background: white;
    display: flex;
    flex-direction: column;
  }
  .lunwenzhixie_box_left {
    min-width: 706px;
    flex: 1;
    height: calc(100%);
    padding: 30px;
    border-radius: 12px;
    background: white;
    display: flex;
    flex-direction: column;
    position: relative;
  }
  .lunwengaixie_box_left_content {
    flex: 1;
    margin-bottom: 30px;
    padding-bottom: 35px;
    border-bottom: 1px solid #f0f0f0;
    position: relative;
    display: flex;
    flex-direction: column;
  }
  .lunwengaixie_box_right {
    max-width: 780px;
    min-width: 410px;
    flex: 1;
    height: calc(100%);
    padding: 30px;
    border-radius: 12px;
    background: white;
    display: flex;
    flex-direction: column;
  }
  .lunwenzhixie_box_right {
    max-width: 780px;
    min-width: 410px;
    flex: 1;
    height: calc(100%);
    padding: 30px;
    border-radius: 12px;
    background: white;
    display: flex;
    flex-direction: column;
  }
  :deep(.lunwengaixie_box_right .el-textarea) {
    flex: 1;
  }
  :deep(.lunwengaixie_box_right .el-textarea textarea) {
    height: 100%;
    resize: none;
    border: none;
  }
  :deep(.lunwenzhixie_box_right .el-textarea) {
    flex: 1;
  }
  :deep(.lunwenzhixie_box_right .el-textarea textarea) {
    height: 100%;
    resize: none;
    padding: 15px;
    border-radius: 6px;
  }

  .lunwengaixie_box_title {
    font-size: 14px;
    margin-bottom: 15px;
  }
  .lunwengaixie_box_title i {
    color: #9992f7;
    font-weight: bolder;
  }
  .lunwenxuxie_box_title {
    font-size: 14px;
    margin-bottom: 15px;
    display: flex;
    justify-content: space-between;
  }
  :deep(.el-select .el-input.is-focus .el-input__inner) {
    border-color: #6965ea;
  }
  :deep(.el-select .el-input:hover) {
    border-color: #6965ea;
  }
  .lunwenxuxie_box_title i {
    color: #9992f7;
    font-weight: bolder;
  }
  .lunwengaixie_box_left_remark_box {
    width: 100%;
    height: 160px;
    background: #ffffff;
    border-radius: 8px;
    border: 1px solid #dedede;
    box-sizing: border-box;
    padding: 14px 16px;
    display: flex;
    flex-direction: column;
  }
  :deep(.lunwengaixie_box_left_remark_box .el-textarea textarea) {
    height: 92px;
    resize: none;
    border: none;
  }
  .lunwengaixie_box_left_content_number {
    width: 100%;
    font-size: 12px;
    position: absolute;
    bottom: 0;
    align-items: center;
    height: 35px;
  }
  .lunwengaixie_box_left_content_number_inside {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }
  .lunwenxuxie_box_left_content_number {
    width: calc(100% - 28px);
  }
  .lunwengaixie_box_left_content_number span {
    margin-right: 7px;
  }
  .lunwengaixie_box_left_content_number i {
    cursor: pointer;
  }
  :deep(.lunwengaixie_box_left_content .el-textarea) {
    height: 100%;
  }
  :deep(.lunwengaixie_box_left_content .el-textarea textarea) {
    /* height: 92px; */
    height: 100%;
    resize: none;
    border: none;
  }
  .lunwenxuxie_box_content {
    flex: 1;
    margin-bottom: 30px;
    padding: 20px 8px 37px 20px;
    border: 1px solid #f0f0f0;
    position: relative;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    border-radius: 8px;
    height: calc(100% - 280px);
  }
  :deep(.lunwenxuxie_box_content .el-textarea) {
    flex: 1;
  }
  :deep(.lunwenxuxie_box_content .el-textarea textarea) {
    /* height: 92px; */
    height: 100%;
    resize: none;
    border: none;
  }
  .lunwengaixie_box_left_remark_box_tag {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .tag_box {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  .tag_box span {
    font-size: 12px;
    font-weight: 400;
    color: #595959;
    line-height: 17px;
    padding: 5px 12px;
    background: #f6f5fd;
    border-radius: 6px;
    cursor: pointer;
    /* transition: background .3s; */
  }
  .tag_box span:hover {
    background: #edebfa;
  }
  .lunwengaixie_tag_number span {
    font-size: 12px;
    font-weight: 400;
    color: #a1a1a1;
    line-height: 17px;
    margin-right: 15px;
  }
  :deep(.lunwengaixie_tag_number .el-button--primary) {
    background-color: #6965ea;
    border-color: #6965ea;
  }
  :deep(.lunwengaixie_tag_number .el-button--primary:hover) {
    background-color: #6965ea;
    border-color: #6965ea;
  }
  :deep(.lunwenzhixie_form .el-form-item label) {
    color: rgba(0, 0, 0, 0.88);
    font-weight: 700;
  }
  .lunwenzhixie_form {
    padding-top: 20px;
  }
  :deep(.lunwenzhixie_form .el-form-item .el-input .el-input__inner) {
    border-radius: 8px;
    height: 46px;
  }
  :deep(
      .lunwenzhixie_form .el-form-item .el-select .el-input .el-input__inner
    ) {
    border-radius: 6px;
    height: 32px;
  }
  :deep(.lunwenzhixie_form .el-form-item .el-input .el-input__inner:hover) {
    border: 1px solid #6965ea;
  }
  :deep(.lunwenzhixie_form .el-form-item .el-input .el-input__inner:focus) {
    border: 1px solid #6965ea;
  }
  :deep(.lunwenzhixie_form .el-form-item .el-textarea .el-textarea__inner) {
    border-radius: 6px;
    resize: none;
    height: 236px;
  }
  :deep(
      .lunwenzhixie_form .el-form-item .el-textarea .el-textarea__inner:hover
    ) {
    border: 1px solid #6965ea;
  }
  :deep(
      .lunwenzhixie_form .el-form-item .el-textarea .el-textarea__inner:focus
    ) {
    border: 1px solid #6965ea;
  }
  .lunwenzhixie_form_btn .el-form-item__content {
    position: relative;
  }
  .lunwenzhixie_form_btn
    .el-form-item__content
    .lunwenzhixie_form_btn_position_left {
    position: absolute;
  }
  .lunwenzhaiyao_form_btn .el-form-item__content {
    position: relative;
  }
  .lunwenzhaiyao_form_btn
    .el-form-item__content
    .lunwenzhixie_form_btn_position_left {
    position: absolute;
  }
  .lunwenzhaiyao_form_btn {
    position: absolute;
    width: 100%;
    bottom: 0;
  }
  .lunwenzhixie_form_btn_box {
    height: 42px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .lunwenzhixie_form_btn_box .el-button {
    width: 142px;
    height: 36px;
    line-height: 18px;
    background-color: #6965ea;
    border-color: #6965ea;
  }
  .el-select-dropdown__item.selected {
    color: #6965ea;
  }
  :deep(
      .lunwenzhixie_form
        .el-form-item
        .el-radio-group
        .el-radio.is-bordered.is-checked
    ) {
    border-color: #6965ea;
    border-radius: 8px;
  }
  :deep(.lunwenzhixie_form .el-form-item .el-radio-group label) {
    border-radius: 8px;
  }
  :deep(
      .lunwenzhixie_form
        .el-form-item
        .el-radio-group
        .el-radio__input.is-checked
        + .el-radio__label
    ) {
    color: #6965ea;
  }
  :deep(
      .lunwenzhixie_form
        .el-form-item
        .el-radio-group
        .el-radio__input.is-checked
        .el-radio__inner
    ) {
    border-color: #6965ea;
    background: #6965ea;
  }

  .chat-box-container {
    height: calc(100vh - 60px);
    width: 100%;
  }
  .chat-box-container .chat-box-container-box {
    display: -webkit-flex;
    display: flex;
    width: 100%;
    height: 100%;
  }
  .chat-box-container .chat-box-right {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    overflow-y: auto;
    position: relative;
    overflow-x: auto;
    -webkit-flex: 1 1;
    flex: 1 1;
  }
  .copywriting-page {
    display: flex;
    width: 100%;
    height: calc(100% - 54px);
    background: #f7f7f8;
  }

  .copywriting-page__content-wrap {
    flex: 1 1;
    overflow: hidden;
    position: relative;
  }
  .copywriting-page__content {
    display: flex;
    flex-direction: column;
    margin: auto;
    height: 100%;
    overflow: auto;
  }
  .copywriting-page-header {
    display: flex;
    padding: 27px 0 19px;
    font-size: 20px;
    color: #000;
    border-bottom: 1px solid #dedede;
    font-weight: 550;
    width: 738px;
    margin: auto;
    line-height: 46px;
  }
  .copywriting-page-header__redbook {
    position: relative;
    top: 1px;
    height: 46px;
    margin-left: 8px;
  }
  .copywritin-content {
    padding-top: 40px;
    -webkit-flex: 1 1;
    flex: 1 1;
    overflow-y: auto;
  }
  .copywriting-page__content .copywriting-header,
  .copywriting-page__content .copywritin-content > div {
    width: 738px;
    margin-left: auto;
    margin-right: auto;
  }
  .copywriting__model-select {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: flex-start;
    align-items: flex-start;
  }
  .copywriting__model-select__label {
    font-size: 13px;
    line-height: 18px;
    color: #000;
    margin-bottom: 16px;
  }
  .copywriting__model-select__options {
    display: -webkit-flex;
    display: flex;
    padding: 6px 8px;
    max-height: 300px;
    overflow-y: auto;
    background: #ffffff;
    border-radius: 8px;
    opacity: 1;
    border: 1px solid #eaeaea;
  }
  .copywriting__model-select__option.active {
    background: #0d0f10;
    color: #fff;
  }
  .copywriting__model-select__option {
    display: -webkit-flex;
    display: flex;
    align-items: center;
    height: 40px;
    padding: 0 34px;
    border-radius: 8px;
    cursor: pointer;
  }
  .copywriting-input__label span {
    color: #ee6360;
    margin-right: 4px;
  }
  .copywriting-input {
    margin-top: 40px;
    position: relative;
  }
  .copywriting-input__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-input__input {
    margin-top: 12px;
    font-size: 13px;
  }
  .copywriting-select {
    margin-top: 40px;
    font-size: 13px;
  }
  .copywriting-select__content {
    position: relative;
  }
  .copywriting-select__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-select__selected {
    margin-top: 12px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    padding: 0 12px;
    height: 40px;
    background: #ffffff;
    border-radius: 4px;
    border: 1px solid #e2e2e2;
    cursor: pointer;
  }
  .copywriting-select__selected .placeholder {
    color: #a8a8a8;
  }

  @keyframes toShow {
    0% {
      bottom: 0px;
      opacity: 0;
    }
    100% {
      bottom: -6px;
      opacity: 1;
    }
  }

  .copywriting-select__options {
    position: absolute;
    z-index: 1;
    bottom: -6px;
    width: 100%;
    padding: 12px 12px 24px 14px;
    -webkit-transform: translateY(100%);
    transform: translateY(100%);
    background: #ffffff;
    box-shadow: 0 8px 12px 1px #00000014;
    border-radius: 8px;
    border: 1px solid #e2e2e2;
    -webkit-animation: toShow 0.2s ease-in;
    animation: toShow 0.2s ease-in;
  }
  .copywriting-select__options-list {
    margin: -4px;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-wrap: wrap;
    flex-wrap: wrap;
  }
  .copywriting-select__options-item {
    position: relative;
    min-width: 88px;
    padding: 0 10px;
    margin: 4px;
    height: 36px;
    text-align: center;
    line-height: 36px;
    background: #f1f1f1;
    border-radius: 6px;
    font-size: 13px;
    color: #434343;
    border: 1px solid transparent;
    cursor: pointer;
  }
  .copywriting-select__options-item.active {
    border-color: #6663e5;
    background: rgba(105, 101, 234, 0.12);
  }
  .copywriting-select__options-item.custom {
    border: 1px dashed #d9d9d9;
    background-color: #fff;
  }
  .copywriting-textarea {
    margin-top: 40px;
    position: relative;
  }
  .copywriting-textarea__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-textarea__content {
    position: relative;
  }
  .copywriting-textarea__textarea {
    max-height: 200px;
    margin-top: 12px;
    min-height: 137px !important;
    font-size: 13px;
  }

  :deep .copywriting-textarea__textarea .el-textarea__inner {
    height: 137px;
  }
  .copywriting__submit {
    margin-top: 20px;
    padding-bottom: 38px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;
  }
  .copywriting__submit > button {
    width: 162px;
    height: 42px;
    background: linear-gradient(267deg, #6468e7 0%, #8a55fe 100%);
  }
  .copywriting-select__options-item-status {
    position: absolute;
    bottom: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .copywriting-select__options-item-status img {
    width: 16px;
    height: 14px;
  }
  .copywriting-select__options-item-status i {
    position: absolute;
    bottom: 0;
    font-size: 12px;
    color: #fff;
  }

  .chat-box-container .chat-box-container-box {
    display: -webkit-flex;
    display: flex;
    width: 100%;
    height: 100%;
  }
  .chat-box-container .chat-box-right {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    overflow-y: auto;
    position: relative;
    overflow-x: auto;
    -webkit-flex: 1 1;
    flex: 1 1;
  }
  .copywriting-page {
    display: flex;
    width: 100%;
    height: calc(100% - 54px);
    background: #f7f7f8;
  }

  .copywriting-page__content-wrap {
    flex: 1 1;
    overflow: hidden;
    position: relative;
  }
  .copywriting-page__content {
    display: flex;
    flex-direction: column;
    margin: auto;
    height: 100%;
    overflow: auto;
  }
  .copywriting-page-header {
    display: flex;
    padding: 27px 0 19px;
    font-size: 20px;
    color: #000;
    border-bottom: 1px solid #dedede;
    font-weight: 550;
    width: 738px;
    margin: auto;
    line-height: 46px;
  }
  .copywriting-page-header__redbook {
    position: relative;
    top: 1px;
    height: 46px;
    margin-left: 8px;
  }
  .copywritin-content {
    padding-top: 40px;
    -webkit-flex: 1 1;
    flex: 1 1;
    overflow-y: auto;
  }
  .copywriting-page__content .copywriting-header,
  .copywriting-page__content .copywritin-content > div {
    width: 738px;
    margin-left: auto;
    margin-right: auto;
  }
  .copywriting__model-select {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: flex-start;
    align-items: flex-start;
  }
  .copywriting__model-select__label {
    font-size: 13px;
    line-height: 18px;
    color: #000;
    margin-bottom: 16px;
  }
  .copywriting__model-select__options {
    display: -webkit-flex;
    display: flex;
    padding: 6px 8px;
    max-height: 300px;
    overflow-y: auto;
    background: #ffffff;
    border-radius: 8px;
    opacity: 1;
    border: 1px solid #eaeaea;
  }
  .copywriting__model-select__option.active {
    background: #0d0f10;
    color: #fff;
  }
  .copywriting__model-select__option {
    display: -webkit-flex;
    display: flex;
    align-items: center;
    height: 40px;
    padding: 0 34px;
    border-radius: 8px;
    cursor: pointer;
  }
  .copywriting-input__label span {
    color: #ee6360;
    margin-right: 4px;
  }
  .copywriting-input {
    margin-top: 40px;
    position: relative;
  }
  .copywriting-input__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }

  .copywriting-select {
    margin-top: 40px;
    font-size: 13px;
  }
  .copywriting-select__content {
    position: relative;
  }
  .copywriting-select__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-select__selected {
    margin-top: 12px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    padding: 0 12px;
    height: 40px;
    background: #ffffff;
    border-radius: 4px;
    border: 1px solid #e2e2e2;
    cursor: pointer;
  }
  .copywriting-select__selected .placeholder {
    color: #a8a8a8;
  }

  @keyframes toShow {
    0% {
      bottom: 0px;
      opacity: 0;
    }
    100% {
      bottom: -6px;
      opacity: 1;
    }
  }

  .copywriting-select__options {
    position: absolute;
    z-index: 1;
    bottom: -6px;
    width: 100%;
    -webkit-transform: translateY(100%);
    transform: translateY(100%);
    background: #ffffff;
    box-shadow: 0 8px 12px 1px #00000014;
    border-radius: 8px;
    border: 1px solid #e2e2e2;
    -webkit-animation: toShow 0.2s ease-in;
    animation: toShow 0.2s ease-in;
  }
  .copywriting-select__options-list {
    margin: -4px;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-wrap: wrap;
    flex-wrap: wrap;
  }
  .copywriting-select__options-item {
    position: relative;
    min-width: 88px;
    padding: 0 10px;
    margin: 4px;
    height: 36px;
    text-align: center;
    line-height: 36px;
    background: #f1f1f1;
    border-radius: 6px;
    font-size: 13px;
    color: #434343;
    border: 1px solid transparent;
    cursor: pointer;
  }
  .copywriting-select__options-item.active {
    border-color: #6663e5;
    background: rgba(105, 101, 234, 0.12);
  }
  .copywriting-select__options-item.custom {
    border: 1px dashed #d9d9d9;
    background-color: #fff;
  }
  .copywriting-textarea {
    margin-top: 40px;
    position: relative;
  }
  .copywriting-textarea__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-textarea__content {
    position: relative;
  }
  .copywriting-textarea__textarea {
    max-height: 200px;
    margin-top: 12px;
    min-height: 137px !important;
    font-size: 13px;
  }

  :deep .copywriting-textarea__textarea .el-textarea__inner {
    height: 137px;
  }
  .copywriting__submit {
    margin-top: 20px;
    padding-bottom: 38px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;
  }
  .copywriting__submit > button {
    width: 162px;
    height: 42px;
    background: linear-gradient(267deg, #6468e7 0%, #8a55fe 100%);
  }
  .copywriting-select__options-item-status {
    position: absolute;
    bottom: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .copywriting-select__options-item-status img {
    width: 16px;
    height: 14px;
  }
  .copywriting-select__options-item-status i {
    position: absolute;
    bottom: 0;
    font-size: 12px;
    color: #fff;
  }

  .copywriting-line-select__delete {
    position: absolute;
    top: 38px;
    right: -29px;
    text-align: center;
    line-height: 24px;
    width: 24px;
    height: 24px;
    cursor: pointer;
    color: red;
    font-size: 14px;
  }
  .copywriting-add-more-tools {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-wrap: wrap;
    flex-wrap: wrap;
    padding-top: 20px;
    margin: -5px;
  }
  .copywriting-add-more-tools__item {
    white-space: nowrap;
    padding: 8px 12px;
    line-height: 17px;
    font-size: 12px;
    color: #141414;
    border-radius: 6px;
    border: 1px solid transparent;
    margin: 5px;
    cursor: pointer;
  }

  .copywriting-add-more-tools__item i {
    font-size: 14px;
    margin-right: 4px;
    color: #7c7e83;
  }

  .copywriting-line-select__options-item {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    height: 40px;
    padding: 0 16px;
    border-radius: 8px;
    font-size: 13px;
    color: #141414;
    cursor: pointer;
  }
  .copywriting-line-select__options-item.active,
  .copywriting-line-select__options-item:hover {
    background: #f7f7f7;
    color: #6663e5;
  }
  .copywriting-line-select__options {
    position: absolute;
    z-index: 1;
    bottom: -6px;
    width: 100%;
    max-height: 300px;
    overflow-y: auto;
    -webkit-transform: translateY(100%);
    transform: translateY(100%);
    background: #ffffff;
    box-shadow: 0 8px 12px 1px #00000014;
    border-radius: 8px;
    -webkit-animation: toShow 0.2s ease-in;
    animation: toShow 0.2s ease-in;
    border: 1px solid #e2e2e2;
  }
  .copywriting-line-select__content {
    position: relative;
  }
  .copywriting-select__options-tools {
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: flex-end;
    justify-content: flex-end;
    font-size: 14px;
    padding: 5px;
    margin-bottom: 6px;
    color: #727272;
  }
  .copywriting-select__options-tools i {
    cursor: pointer;
  }
  .page-container {
    padding-top: 60px;
  }

  .translate__container {
    width: 1020px;
    padding: 20px 20px;
    margin: 0 auto;
    background: #fefefe;
    box-shadow: 0 10px 16px 1px #2d2f400a;
    border-radius: 16px;
  }

  .translate__upload {
    padding: 40px 30px;
    background: #fafafc;
    border-radius: 4px;
    border: 1px dashed #d4d4d4;
    display: -webkit-flex;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .translate__upload-btns button {
    width: 144px;
    height: 40px;
    border-radius: 8px;
  }
  .translate__upload-btns .el-button--default {
    color: #7b7b7b;
    border-color: #7b7b7b;
    margin-right: 16px;
  }

  .translate__upload-btns .el-button--default:hover {
    color: #9992f7;
    border-color: #9992f7;
    background-color: transparent;
  }
  .translate__upload-btns button.el-button--primary {
    background: linear-gradient(267deg, #6468e7 0%, #8a55fe 100%);
    border: none;
  }
  .translate__upload .translate__upload-title {
    color: #000;
    font-size: 14px;
  }

  .translate__upload .translate__upload-btns {
    margin-top: 24px;
    display: flex;
  }

  .translate__upload .translate__upload-description {
    width: 100%;
    margin-top: 20px;
    padding-top: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 13px;
    color: #8f8f8f;
    margin-bottom: 20px;
  }

  .translate__upload
    .translate__upload-description
    .translate__upload-description-item {
    margin-right: 17px;
    position: relative;
    display: flex;
  }

  .translate__upload
    .translate__upload-description
    .translate__upload-description-item:nth-child(3) {
    margin-right: 0;
  }

  .translate__upload .translate__upload-description-item-tooltip {
    position: absolute;
    width: 56px;
    height: 24px;
    border-radius: 6px;
    border: 1px solid #eeeeee;
    bottom: -27px;
    left: -5px;
    display: flex;
    align-items: center;
    font-weight: 400;
    font-size: 12px;
    color: #4d4d4d;
    line-height: 24px;
    justify-content: center;
  }

  .translate__upload-description-item-tooltip :before {
    content: '';
    width: 5px;
    height: 5px;
    position: absolute;
    top: -4px;
    left: 8px;
    background: #fff;
    border-left: 1px solid #eeeeee;
    border-top: 1px solid #eeeeee;
    transform: rotate(45deg);
  }

  .translate__upload .translate__upload-description-line {
    width: 2px;
    height: 17px;
    background: #d4d4d4;
    margin: 0 16px;
    border-radius: 18px;
  }

  .icon-pdf,
  .icon-word {
    line-height: 18px;
    margin-right: 6px;
  }

  .translate__discription {
    display: flex;
    width: 1060px;
    margin: 50px auto;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 16px;
    border: 1px solid #eef3fa;
  }

  .translate__discription-item-container {
    border-radius: 10px;
    padding: 16px 16px 16px 56px;
    position: relative;
    overflow: hidden;
  }
  .translate__discription-item-title {
    font-size: 14px;
    color: #141414;
    line-height: 18px;
  }
  .translate__discription-item-desc {
    margin-top: 6px;
    font-size: 12px;
    color: #595959;
  }

  .translate__upload-description-item.active {
    box-shadow: 0 8px 20px 1px #2d2f4014;
  }
  .translate__discription-item {
    position: relative;
    width: 372px;
    cursor: pointer;
    border-radius: 10px;
  }

  .translate__discription-item .icon {
    position: absolute;
    left: 29px;
    line-height: 1;
    top: 18px;
  }

  .translate__discription-item.active
    .translate__discription-item-container:before {
    content: '';
    display: block;
    position: absolute;
    left: 0;
    top: 0;
    width: 3px;
    height: 100%;
    background: #aba8fe;
  }
  .translate__discription-item.active :before {
    position: absolute;
    left: 29px;
    line-height: 1;
    top: 18px;
  }
  .translate__discription-item.active .translate__discription-item-title {
    color: #6965ea;
  }

  .translate__discription-img {
    flex: 1 1;
    background-repeat: no-repeat;
    background-position: center center;
    background-size: contain;
  }

  .translate__discription-img.science {
    background-size: initial;
  }

  .main-container {
    display: flex;
    width: 100%;
    height: calc(100vh - 54px);
  }

  .box-left {
    display: flex;
  }
  .left-history-x-general-btns {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: center;
    align-items: center;
    background: #ffffff;
    box-shadow: -4px 0 16px 1px #3634651a;
    border-radius: 0 8px 8px 0;
    width: 0px;
    -webkit-flex: 0 0 46px;
    flex: 0 0 46px;
    border: 1px solid #e5e5e5;
    border-left: none;
    position: absolute;
    top: 62px;
    z-index: 6;
  }
  .left-history-x-general-btns.show {
    width: 46px;
  }
  .left-history-x-general-btns.hide {
    width: 0;
    overflow: hidden;
  }
  .left-history-x-general-btns .ssb-eh__item {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: center;
    align-items: center;
    color: #141414;
    padding: 12px;
    position: relative;
    cursor: pointer;
  }
  .left-history-x-general-btns .ssb-eh__item-text {
    font-size: 12px;
    line-height: 16px;
    white-space: nowrap;
    margin-top: 4px;
  }
  .left-history-x-general-btns .ssb-eh__item-icon {
    font-size: 13px;
    line-height: 13px;
  }

  .left-history-x-general-listBox {
    display: -webkit-flex;
    display: flex;
    height: 100%;
    background: #ffffff;
    z-index: 7;
    position: relative;
    transition: all 0.2s;
    box-shadow: 2px 0 12px 1px #0000000f;
  }
  .left-history-x-general-listBox .history-x-listBoxs.show {
    opacity: 1;
    transition: all 0.6s ease-in;
  }
  .left-history-x-general-listBox .history-x-listBoxs.hide {
    opacity: 0;
    transition: all 0.3s;
  }
  .left-history-x-general-listBox.hide {
    width: 0;
    overflow: hidden;
  }
  .left-history-x-general-listBox .history-x-listBoxs {
    width: 270px;
    height: 100%;
    position: absolute;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
  }
  .left-history-x-general-listBox .history-x-listBoxs .lb-chat-left-btn {
    padding: 10px 12px;
    color: #fff;
    cursor: pointer;
    border: 1px solid #4d4d4f;
    border-radius: 8px;
    margin: 20px 20px 8px;
    background-color: #0d0f10;
    text-align: center;
  }
  .left-history-x-general-listBox .history-x-listBoxs .lb-chat-left-btn i {
    margin-right: 5px;
  }
  .history-com-x-general-list {
    -webkit-flex: 1 1;
    flex: 1 1;
    padding: 0 12px;
    -webkit-overflow-scrolling: touch;
    overscroll-behavior: none;
    overflow-y: auto;
  }
  .ssb-hl-general-list-empty {
    display: -webkit-flex;
    display: flex;
    height: 100%;
    width: 100%;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
  }
  .ssb-hl-general-list-empty__container {
    width: 132px;
    text-align: center;
  }

  .ssb-hl-general-list-empty__img {
    width: 100%;
  }
  .ssb-hl-general-list-empty__img img {
    max-width: 100%;
  }
  .ssb-hl-general-list-empty__text {
    margin-top: 8px;
    font-size: 12px;
    color: #8c8c8c;
    line-height: 20px;
  }
  .left-history-x-general-listBox.show {
    width: 270px;
  }
  .left-history-x-general-listBox .history-x__close {
    width: 20px;
    height: 32px;
    text-align: center;
    line-height: 27px;
    background: #ffffff;
    box-shadow: 4px 0 16px 1px #3634651a;
    border-radius: 0 6px 6px 0;
    border: 1px solid #e5e5e5;
    border-left: none;
    cursor: pointer;
    color: #141414;
    position: absolute;
    right: -20px;
    top: 62px;
  }

  .left-history-x-general-btns .ssb-eh__item:nth-child(1):before {
    content: none;
  }
  .left-history-x-general-btns .ssb-eh__item:before {
    content: '';
    position: absolute;
    top: 0px;
    left: 12px;
    right: 12px;
    height: 1px;
    background: #d0d0d0;
  }
  .paper-abstract {
    height: 100%;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    gap: 30px;
    background: #f2f2f5;
    padding: 30px 30px 30px 56px;
  }
  .main-container .box-right {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    overflow-y: auto;
    position: relative;
    overflow-x: auto;
    -webkit-flex: 1 1;
    flex: 1 1;
  }

  .paper-abstract-form-card {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    width: 833px;
    min-width: 620px;
    padding: 0 30px 30px;
    height: 100%;
    background: #ffffff;
    border-radius: 12px;
    font-size: 14px;
    color: #141414;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .paper-abstract-form-card__header {
    margin-top: 16px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    width: 100%;
  }
  .paper-abstract-form-card__header-title {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    font-weight: 700;
    gap: 8px;
  }

  .paper-abstract-upload {
    height: 201px;
    cursor: not-allowed;
  }

  :deep .paper-abstract-upload.disabled .el-upload {
    background-color: #fafbfc !important;
  }

  :deep .paper-abstract-upload .el-upload {
    width: 100% !important;
    height: 100% !important;
    background-color: transparent !important;
  }

  :deep .paper-abstract-upload .el-upload .el-upload-dragger {
    width: 100% !important;
    height: 100% !important;
    background-color: #fafbfc !important;
    -webkit-user-select: none;
    user-select: none;
  }

  :deep .el-tabs__nav-wrap::after {
    content: none;
  }

  .paper-abstract-upload__placeholder {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }

  .paper-abstract-form-card__models {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    max-width: 264px;
    height: 34px;
    padding: 4px;
    background: #f7f7f7;
    border-radius: 6px;
    -webkit-user-select: none;
    user-select: none;
  }
  .paper-abstract-form-card__model {
    width: -moz-max-content;
    width: -webkit-max-content;
    width: max-content;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    gap: 5px;
    height: 100%;
    font-size: 13px;
    color: #595959;
    padding: 0 6px;
    cursor: pointer;
  }
  .paper-abstract-form-card__model.active {
    background: #ffffff;
    box-shadow: 0 2px 6px 1px #1a1a1d14;
    border-radius: 2px;
    color: #6965ea;
  }

  .paper-abstract-form-card__form {
    margin-top: 20px;
    flex: 1 1;
    display: -webkit-flex;
    display: flex;
    gap: 25px;
    flex-direction: column;
  }

  :deep .paper-abstract-article-input .el-tabs__header {
    margin-bottom: 0;
  }

  .paper-abstract-article-input__tab-label {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    gap: 6px;
    color: #141414;
    font-size: 13px;
    font-weight: 700;
    font-family: PingFang SC, PingFang SC;
  }
  .paper-abstract-form-card__form-item {
    position: relative;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
  }
  .paper-abstract-form-card__form-item-label {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    font-size: 13px;
    color: #141414;
    font-weight: 700;
  }
  .paper-abstract-form-card__generate-mode {
    margin-top: 10px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
  }

  .paper-abstract-form-card__generate-mode .el-radio {
    margin-right: 10px;
  }

  .paper-abstract-article-input .chat-paper-form-item {
    height: 254px !important;
  }

  :deep
    .paper-abstract-article-input
    .chat-paper-form-item
    .el-textarea__inner {
    height: 100%;
  }
  :deep .paper-abstract-form-card__form .el-form-item__label {
    font-weight: 600;
    color: #000;
    padding-bottom: 0;
  }

  :deep .paper-abstract-form-card__form textarea {
    min-height: 100px;
    height: 64px;
    resize: 'none';
  }
  .paper-abstract-form-card__footer {
    position: relative;
    padding: 0 180px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;
    -webkit-align-items: center;
    align-items: center;
  }
  .paper-abstract-form-card__wordcount-select {
    position: absolute;
    left: 0;
    top: 50%;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
  }
  .paper-abstract-form-card__submit-btn {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    width: 140px;
    height: 36px;
    background: #6965ea;
    border-radius: 6px;
    font-size: 14px;
    color: #fff;
    cursor: pointer;
    border: none;
  }

  .paper-abstract-result-card {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    width: 833px;
    min-width: 370px;
    padding: 0 30px 30px;
    height: 100%;
    background: #ffffff;
    border-radius: 12px;
    font-size: 14px;
    color: #141414;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .paper-abstract-result-card__head {
    height: 64px;
    -webkit-flex-shrink: 0;
    flex-shrink: 0;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    width: 100%;
  }
  .paper-abstract-result-card__head-title {
    position: relative;
    font-weight: 700;
    padding-left: 12px;
  }
  .paper-abstract-result-card__head-title:before {
    content: '';
    position: absolute;
    left: 0;
    top: 50%;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    width: 5px;
    height: 12px;
    background: #9694f2;
    border-radius: 1px 6px 6px 1px;
  }
  .paper-abstract-result-card__body {
    position: relative;
    -webkit-flex: 1 1;
    flex: 1 1;
    background: #ffffff;
    font-size: 14px;
    color: #141414;
    line-height: 24px;
  }
  .paper-abstract-result-card__result-empty {
    position: absolute;
    top: 20px;
    left: 20px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    color: #bfbfbf;
    gap: 3px;
    z-index: 2;
  }

  :deep .paper-abstract-result-card__result {
    height: 100%;
  }
  :deep .paper-abstract-result-card__result .el-textarea__inner {
    border-color: #d9d9d9 !important;
    height: 100% !important;
    padding: 20px 20px 40px !important;
  }

  .paper-abstract-article-input__vip-popup {
    position: relative;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: center;
    align-items: center;
    width: 246px;
    height: 143px;
    background: #fffbf5;
    box-shadow: 0 2px 28px 1px #00000024;
    border-radius: 12px;
    border: 1px solid #ffffff;
  }

  .paper-abstract-article-input__close-btn {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    position: absolute;
    top: 10px;
    right: 10px;
    width: 16px;
    height: 16px;
    font-size: 12px;
    cursor: pointer;
    color: #b4a896;
  }
  .paper-abstract-article-input__vip-txt1 {
    margin-top: 27px;
    font-size: 13px;
    font-weight: 700;
    color: #141414;
  }
  .paper-abstract-article-input__vip-txt2 {
    margin-top: 6px;
    font-size: 12px;
    color: #727272;
  }
  .paper-abstract-article-input__vip-button {
    margin-top: 19px;
    outline: none;
    border: none;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    width: 102px;
    height: 32px;
    background: linear-gradient(127deg, #ffb857 0%, #eda05e 100%);
    box-shadow: 0 6px 16px 1px #af681f29;
    border-radius: 7px;
    gap: 6px;
    font-size: 12px;
    font-family: PingFang SC, PingFang SC;
    color: #fff;
    cursor: pointer;
  }

  :deep .el-popover {
    padding: 0;
    background: transparent;
  }

  .main-container {
    display: flex;
    width: 100%;
    height: calc(100% - 54px);
    background-color: #f2f2f5;
  }

  .box-left {
    display: flex;
  }
  .left-history-x-general-btns {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: center;
    align-items: center;
    background: #ffffff;
    box-shadow: -4px 0 16px 1px #3634651a;
    border-radius: 0 8px 8px 0;
    width: 0px;
    -webkit-flex: 0 0 46px;
    flex: 0 0 46px;
    border: 1px solid #e5e5e5;
    border-left: none;
    position: absolute;
    top: 62px;
    z-index: 6;
  }
  .left-history-x-general-btns.show {
    width: 46px;
  }
  .left-history-x-general-btns.hide {
    width: 0;
    overflow: hidden;
  }
  .left-history-x-general-btns .ssb-eh__item {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: center;
    align-items: center;
    color: #141414;
    padding: 12px;
    position: relative;
    cursor: pointer;
  }
  .left-history-x-general-btns .ssb-eh__item-text {
    font-size: 12px;
    line-height: 16px;
    white-space: nowrap;
    margin-top: 4px;
  }
  .left-history-x-general-btns .ssb-eh__item-icon {
    font-size: 13px;
    line-height: 13px;
  }

  .left-history-x-general-listBox {
    display: -webkit-flex;
    display: flex;
    height: 100%;
    background: #ffffff;
    z-index: 7;
    position: relative;
    transition: all 0.2s;
    box-shadow: 2px 0 12px 1px #0000000f;
  }
  .left-history-x-general-listBox .history-x-listBoxs.show {
    opacity: 1;
    transition: all 0.6s ease-in;
  }
  .left-history-x-general-listBox .history-x-listBoxs.hide {
    opacity: 0;
    transition: all 0.3s;
  }
  .left-history-x-general-listBox.hide {
    width: 0;
    overflow: hidden;
  }
  .left-history-x-general-listBox .history-x-listBoxs {
    width: 270px;
    height: 100%;
    position: absolute;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
  }
  .left-history-x-general-listBox .history-x-listBoxs .lb-chat-left-btn {
    padding: 10px 12px;
    color: #fff;
    cursor: pointer;
    border: 1px solid #4d4d4f;
    border-radius: 8px;
    margin: 20px 20px 8px;
    background-color: #0d0f10;
    text-align: center;
  }
  .left-history-x-general-listBox .history-x-listBoxs .lb-chat-left-btn i {
    margin-right: 5px;
  }
  .history-com-x-general-list {
    -webkit-flex: 1 1;
    flex: 1 1;
    padding: 0 12px;
    -webkit-overflow-scrolling: touch;
    overscroll-behavior: none;
    overflow-y: auto;
  }
  .ssb-hl-general-list-empty {
    display: -webkit-flex;
    display: flex;
    height: 100%;
    width: 100%;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
  }
  .ssb-hl-general-list-empty__container {
    width: 132px;
    text-align: center;
  }

  .ssb-hl-general-list-empty__img {
    width: 100%;
  }
  .ssb-hl-general-list-empty__img img {
    max-width: 100%;
  }
  .ssb-hl-general-list-empty__text {
    margin-top: 8px;
    font-size: 12px;
    color: #8c8c8c;
    line-height: 20px;
  }
  .left-history-x-general-listBox.show {
    width: 270px;
  }
  .left-history-x-general-listBox .history-x__close {
    width: 20px;
    height: 32px;
    text-align: center;
    line-height: 27px;
    background: #ffffff;
    box-shadow: 4px 0 16px 1px #3634651a;
    border-radius: 0 6px 6px 0;
    border: 1px solid #e5e5e5;
    border-left: none;
    cursor: pointer;
    color: #141414;
    position: absolute;
    right: -20px;
    top: 62px;
  }

  .left-history-x-general-btns .ssb-eh__item:nth-child(1):before {
    content: none;
  }
  .left-history-x-general-btns .ssb-eh__item:before {
    content: '';
    position: absolute;
    top: 0px;
    left: 12px;
    right: 12px;
    height: 1px;
    background: #d0d0d0;
  }
  .paper-thanks {
    height: 100%;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    gap: 30px;
    background: #f2f2f5;
    padding: 30px 30px 30px 56px;
  }
  .main-container .box-right {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    overflow-y: auto;
    position: relative;
    overflow-x: auto;
    -webkit-flex: 1 1;
    flex: 1 1;
  }

  .paper-thanks-form-card {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    width: 833px;
    min-width: 620px;
    padding: 0 30px 30px;
    height: 100%;
    background: #ffffff;
    border-radius: 12px;
    font-size: 14px;
    color: #141414;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .paper-thanks-form-card__header {
    margin-top: 16px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    width: 100%;
  }
  .paper-thanks-form-card__header-title {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    font-weight: 700;
    gap: 8px;
  }
  .paper-thanks-form-card__models {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    max-width: 264px;
    height: 34px;
    padding: 4px;
    background: #f7f7f7;
    border-radius: 6px;
    -webkit-user-select: none;
    user-select: none;
  }
  .paper-thanks-form-card__model {
    width: -moz-max-content;
    width: -webkit-max-content;
    width: max-content;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    gap: 5px;
    height: 100%;
    font-size: 13px;
    color: #595959;
    padding: 0 6px;
    cursor: pointer;
  }
  .paper-thanks-form-card__model.active {
    background: #ffffff;
    box-shadow: 0 2px 6px 1px #1a1a1d14;
    border-radius: 2px;
    color: #6965ea;
  }

  .paper-thanks-form-card__form {
    margin-top: 30px;
  }

  :deep .paper-thanks-form-card__form .el-form-item__label {
    font-weight: 600;
    color: #000;
    padding-bottom: 0;
  }

  :deep .paper-thanks-form-card__form textarea {
    min-height: 100px;
    height: 25vh;
    resize: 'none';
  }
  .paper-thanks-form-card__footer {
    position: relative;
    padding: 0 180px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;
    -webkit-align-items: center;
    align-items: center;
  }
  .paper-thanks-form-card__wordcount-select {
    position: absolute;
    left: 0;
    top: 50%;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
  }
  .paper-thanks-form-card__submit-btn {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    width: 140px;
    height: 36px;
    background: #6965ea;
    border-radius: 6px;
    font-size: 14px;
    color: #fff;
    cursor: pointer;
    border: none;
  }

  .paper-thanks-result-card {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    width: 833px;
    min-width: 370px;
    padding: 0 30px 30px;
    height: 100%;
    background: #ffffff;
    border-radius: 12px;
    font-size: 14px;
    color: #141414;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .paper-thanks-result-card__head {
    height: 64px;
    -webkit-flex-shrink: 0;
    flex-shrink: 0;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    width: 100%;
  }
  .paper-thanks-result-card__head-title {
    position: relative;
    font-weight: 700;
    padding-left: 12px;
  }
  .paper-thanks-result-card__head-title:before {
    content: '';
    position: absolute;
    left: 0;
    top: 50%;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    width: 5px;
    height: 12px;
    background: #9694f2;
    border-radius: 1px 6px 6px 1px;
  }
  .paper-thanks-result-card__body {
    position: relative;
    -webkit-flex: 1 1;
    flex: 1 1;
    background: #ffffff;
    font-size: 14px;
    color: #141414;
    line-height: 24px;
  }
  .paper-thanks-result-card__result-empty {
    position: absolute;
    top: 20px;
    left: 20px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    color: #bfbfbf;
    gap: 3px;
    z-index: 2;
  }

  :deep .paper-thanks-result-card__result {
    height: 100%;
  }
  :deep .paper-thanks-result-card__result .el-textarea__inner {
    border-color: #d9d9d9 !important;
    height: 100% !important;
    padding: 20px 20px 40px !important;
  }

  .chat-box-container .chat-box-container-box {
    display: -webkit-flex;
    display: flex;
    width: 100%;
    height: 100%;
  }
  .chat-box-container .chat-box-right {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    overflow-y: auto;
    position: relative;
    overflow-x: auto;
    -webkit-flex: 1 1;
    flex: 1 1;
  }
  .copywriting-page {
    display: flex;
    width: 100%;
    height: calc(100% - 54px);
    background: #f7f7f8;
  }

  .copywriting-page__content-wrap {
    flex: 1 1;
    overflow: hidden;
    position: relative;
  }
  .copywriting-page__content {
    display: flex;
    flex-direction: column;
    margin: auto;
    height: 100%;
    overflow: auto;
  }
  .copywriting-page-header {
    display: flex;
    padding: 27px 0 19px;
    font-size: 20px;
    color: #000;
    border-bottom: 1px solid #dedede;
    font-weight: 550;
    width: 738px;
    margin: auto;
    line-height: 46px;
  }
  .copywriting-page-header__redbook {
    position: relative;
    top: 1px;
    height: 46px;
    margin-left: 8px;
  }
  .copywritin-content {
    padding-top: 40px;
    -webkit-flex: 1 1;
    flex: 1 1;
    overflow-y: auto;
  }
  .copywriting-page__content .copywriting-header,
  .copywriting-page__content .copywritin-content > div {
    width: 738px;
    margin-left: auto;
    margin-right: auto;
  }
  .copywriting__model-select {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-direction: column;
    flex-direction: column;
    -webkit-align-items: flex-start;
    align-items: flex-start;
  }
  .copywriting__model-select__label {
    font-size: 13px;
    line-height: 18px;
    color: #000;
    margin-bottom: 16px;
  }
  .copywriting__model-select__options {
    display: -webkit-flex;
    display: flex;
    padding: 6px 8px;
    max-height: 300px;
    overflow-y: auto;
    background: #ffffff;
    border-radius: 8px;
    opacity: 1;
    border: 1px solid #eaeaea;
  }
  .copywriting__model-select__option.active {
    background: #0d0f10;
    color: #fff;
  }
  .copywriting__model-select__option {
    display: -webkit-flex;
    display: flex;
    align-items: center;
    height: 40px;
    padding: 0 34px;
    border-radius: 8px;
    cursor: pointer;
  }
  .copywriting-input__label span {
    color: #ee6360;
    margin-right: 4px;
  }
  .copywriting-input {
    margin-top: 40px;
    position: relative;
    font-size: 13px;
  }
  .copywriting-input__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-input__input {
    margin-top: 12px;
    font-size: 13px;
  }

  :deep .copywriting-input__input .el-textarea__inner {
    max-height: 200px;
    padding: 14px 18px;
    min-height: 137px !important;
    font-size: 13px;
  }
  .copywriting-select {
    margin-top: 40px;
    font-size: 13px;
  }
  .copywriting-select__content {
    position: relative;
  }
  .copywriting-select__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-select__selected {
    margin-top: 12px;
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    padding: 0 12px;
    height: 40px;
    background: #ffffff;
    border-radius: 4px;
    border: 1px solid #e2e2e2;
    cursor: pointer;
  }
  .copywriting-select__selected .placeholder {
    color: #a8a8a8;
    font-size: 13px;
  }

  @keyframes toShow {
    0% {
      bottom: 0px;
      opacity: 0;
    }
    100% {
      bottom: -6px;
      opacity: 1;
    }
  }

  .copywriting-select__options {
    position: absolute;
    z-index: 1;
    bottom: -6px;
    width: 100%;
    -webkit-transform: translateY(100%);
    transform: translateY(100%);
    background: #ffffff;
    box-shadow: 0 8px 12px 1px #00000014;
    border-radius: 8px;
    border: 1px solid #e2e2e2;
    -webkit-animation: toShow 0.2s ease-in;
    animation: toShow 0.2s ease-in;
  }
  .copywriting-select__options-list {
    margin: -4px;
    display: -webkit-flex;
    display: flex;
    -webkit-flex-wrap: wrap;
    flex-wrap: wrap;
  }
  .copywriting-select__options-item {
    position: relative;
    min-width: 88px;
    padding: 0 10px;
    margin: 4px;
    height: 36px;
    text-align: center;
    line-height: 36px;
    background: #f1f1f1;
    border-radius: 6px;
    font-size: 13px;
    color: #434343;
    border: 1px solid transparent;
    cursor: pointer;
  }
  .copywriting-select__options-item.active {
    border-color: #6663e5;
    background: rgba(105, 101, 234, 0.12);
  }
  .copywriting-select__options-item.custom {
    border: 1px dashed #d9d9d9;
    background-color: #fff;
  }
  .copywriting-textarea {
    margin-top: 40px;
    position: relative;
  }
  .copywriting-textarea__label {
    height: 18px;
    font-size: 13px;
    color: #000;
  }
  .copywriting-textarea__content {
    position: relative;
  }
  .copywriting-textarea__textarea {
    max-height: 200px;
    margin-top: 12px;
    min-height: 137px !important;
    font-size: 13px;
  }

  :deep .copywriting-textarea__textarea .el-textarea__inner {
    height: 137px;
  }
  .copywriting__submit {
    margin-top: 20px;
    padding-bottom: 38px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;
  }
  .copywriting__submit > button {
    width: 162px;
    height: 42px;
    background: linear-gradient(267deg, #6468e7 0%, #8a55fe 100%);
  }
  .copywriting-select__options-item-status {
    position: absolute;
    bottom: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .copywriting-select__options-item-status img {
    width: 16px;
    height: 14px;
  }
  .copywriting-select__options-item-status i {
    position: absolute;
    bottom: 0;
    font-size: 12px;
    color: #fff;
  }

  .copywriting-line-select__delete {
    position: absolute;
    top: 38px;
    right: -29px;
    text-align: center;
    line-height: 24px;
    width: 24px;
    height: 24px;
    cursor: pointer;
    color: red;
    font-size: 14px;
  }
  .copywriting-add-more-tools {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-wrap: wrap;
    flex-wrap: wrap;
    padding-top: 20px;
    margin: -5px;
  }
  .copywriting-add-more-tools__item {
    white-space: nowrap;
    padding: 8px 12px;
    line-height: 17px;
    font-size: 12px;
    color: #141414;
    border-radius: 6px;
    border: 1px solid transparent;
    margin: 5px;
    cursor: pointer;
  }

  .copywriting-add-more-tools__item i {
    font-size: 14px;
    margin-right: 4px;
    color: #7c7e83;
  }

  .copywriting-line-select__options-item {
    display: -webkit-flex;
    display: flex;
    -webkit-align-items: center;
    align-items: center;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    height: 40px;
    padding: 0 16px;
    border-radius: 8px;
    font-size: 13px;
    color: #141414;
    cursor: pointer;
  }
  .copywriting-line-select__options-item.active,
  .copywriting-line-select__options-item:hover {
    background: #f7f7f7;
    color: #6663e5;
  }
  .copywriting-line-select__options {
    position: absolute;
    z-index: 1;
    bottom: -6px;
    width: 100%;
    max-height: 300px;
    overflow-y: auto;
    -webkit-transform: translateY(100%);
    transform: translateY(100%);
    background: #ffffff;
    box-shadow: 0 8px 12px 1px #00000014;
    border-radius: 8px;
    -webkit-animation: toShow 0.2s ease-in;
    animation: toShow 0.2s ease-in;
    border: 1px solid #e2e2e2;
  }
  .copywriting-line-select__content {
    position: relative;
  }
  .copywriting-select__options-tools {
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: flex-end;
    justify-content: flex-end;
    font-size: 14px;
    padding: 5px;
    margin-bottom: 6px;
    color: #727272;
  }
  .copywriting-select__options-tools i {
    cursor: pointer;
  }

  .main-container {
    display: flex;
    width: 100%;
    height: calc(100vh);
    background-color: #f2f2f5;
  }

  .box-left {
    display: flex;
  }
  .left-history-x-general-btns {
    display: -webkit-flex;
    display: flex;

    flex-direction: column;

    align-items: center;
    background: #ffffff;
    box-shadow: -4px 0 16px 1px #3634651a;
    border-radius: 0 8px 8px 0;
    width: 0px;
    -webkit-flex: 0 0 46px;
    flex: 0 0 46px;
    border: 1px solid #e5e5e5;
    border-left: none;
    position: absolute;
    top: 62px;
    z-index: 6;
  }
  .left-history-x-general-btns.show {
    width: 46px;
  }
  .left-history-x-general-btns.hide {
    width: 0;
    overflow: hidden;
  }
  .left-history-x-general-btns .ssb-eh__item {
    display: -webkit-flex;
    display: flex;

    flex-direction: column;

    align-items: center;
    color: #141414;
    padding: 12px;
    position: relative;
    cursor: pointer;
  }
  .left-history-x-general-btns .ssb-eh__item-text {
    font-size: 12px;
    line-height: 16px;
    white-space: nowrap;
    margin-top: 4px;
  }
  .left-history-x-general-btns .ssb-eh__item-icon {
    font-size: 13px;
    line-height: 13px;
  }

  .left-history-x-general-listBox {
    display: -webkit-flex;
    display: flex;
    height: 100%;
    background: #ffffff;
    z-index: 7;
    position: relative;
    transition: all 0.2s;
    box-shadow: 2px 0 12px 1px #0000000f;
  }
  .left-history-x-general-listBox .history-x-listBoxs.show {
    opacity: 1;
    transition: all 0.6s ease-in;
  }
  .left-history-x-general-listBox .history-x-listBoxs.hide {
    opacity: 0;
    transition: all 0.3s;
  }
  .left-history-x-general-listBox.hide {
    width: 0;
    overflow: hidden;
  }
  .left-history-x-general-listBox .history-x-listBoxs {
    width: 270px;
    height: 100%;
    position: absolute;
    display: -webkit-flex;
    display: flex;

    flex-direction: column;
  }
  .left-history-x-general-listBox .history-x-listBoxs .lb-chat-left-btn {
    padding: 10px 12px;
    color: #fff;
    cursor: pointer;
    border: 1px solid #4d4d4f;
    border-radius: 8px;
    margin: 20px 20px 8px;
    background-color: #0d0f10;
    text-align: center;
  }
  .left-history-x-general-listBox .history-x-listBoxs .lb-chat-left-btn i {
    margin-right: 5px;
  }
  .history-com-x-general-list {
    -webkit-flex: 1 1;
    flex: 1 1;
    padding: 0 12px;
    -webkit-overflow-scrolling: touch;
    overscroll-behavior: none;
    overflow-y: auto;
  }
  .ssb-hl-general-list-empty {
    display: -webkit-flex;
    display: flex;
    height: 100%;
    width: 100%;

    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
  }
  .ssb-hl-general-list-empty__container {
    width: 132px;
    text-align: center;
  }

  .ssb-hl-general-list-empty__img {
    width: 100%;
  }
  .ssb-hl-general-list-empty__img img {
    max-width: 100%;
  }
  .ssb-hl-general-list-empty__text {
    margin-top: 8px;
    font-size: 12px;
    color: #8c8c8c;
    line-height: 20px;
  }
  .left-history-x-general-listBox.show {
    width: 270px;
  }
  .left-history-x-general-listBox .history-x__close {
    width: 20px;
    height: 32px;
    text-align: center;
    line-height: 27px;
    background: #ffffff;
    box-shadow: 4px 0 16px 1px #3634651a;
    border-radius: 0 6px 6px 0;
    border: 1px solid #e5e5e5;
    border-left: none;
    cursor: pointer;
    color: #141414;
    position: absolute;
    right: -20px;
    top: 62px;
  }

  .left-history-x-general-btns .ssb-eh__item:nth-child(1):before {
    content: none;
  }
  .left-history-x-general-btns .ssb-eh__item:before {
    content: '';
    position: absolute;
    top: 0px;
    left: 12px;
    right: 12px;
    height: 1px;
    background: #d0d0d0;
  }

  .box-right {
    display: -webkit-flex;
    display: flex;

    flex-direction: column;
    overflow-y: auto;
    position: relative;
    overflow-x: auto;
    -webkit-flex: 1 1;
    flex: 1 1;
  }

  .translate_box {
    font-size: 15px;
    font-family: '\82f9\65b9-\7b80-\5e38\89c4\4f53, \82f9\65b9-\7b80';
    font-weight: 400;
    color: #141414;
    line-height: 26px;
    background-color: #f2f2f5;
    height: calc(100% - 54px);
    position: relative;
    display: -webkit-flex;
    display: flex;
  }
  .translate_box .translate_box-content {
    -webkit-flex: 1 1;
    flex: 1 1;
    padding: 56px 30px 30px 56px;
    position: relative;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .chat-translate-header {
    height: 36px;
    opacity: 1;
    position: absolute;
    top: 20px;
    left: 56px;
    display: -webkit-flex;
    display: flex;

    align-items: center;
  }
  .chat-translate-header__tab-item {
    width: 110px;
    height: 36px;
    background: #f9f9fc;
    opacity: 1;
    display: -webkit-flex;
    display: flex;

    align-items: center;
    -webkit-justify-content: center;
    justify-content: center;
    cursor: pointer;
    position: relative;
  }

  .chat-translate-header__tab-item.with-text {
    border-radius: 12px 0 0;
  }
  .chat-translate-header__tab-item.active {
    background: #fff;
    z-index: 2;
  }
  .chat-translate-header__tab-item.active.with-text {
    border-radius: 12px 15px 0 0;
  }
  .chat-translate-header__tab-item.with-image {
    border-radius: 0 15px 0 0;
    margin-left: -6px;
  }
  .chat-translate-header__tab-item.active.with-image {
    border-radius: 15px 15px 0 0;
  }
  .chat-translate-header__tab-text {
    font-size: 14px;
    font-family: '\82f9\65b9-\7b80-\5e38\89c4\4f53, \82f9\65b9-\7b80';
    font-weight: 400;
    color: #595959;
  }
  .chat-translate-header__tab-item.active .chat-translate-header__tab-icon,
  .chat-translate-header__tab-item.active .chat-translate-header__tab-text {
    color: #6965ea;
  }

  .chat-translate-header__tab-icon {
    font-size: 14px;
    margin-right: 8px;
  }
  .chat-translate-header__tab-item.active.with-text:after {
    content: '';
    display: block;
    position: absolute;
    right: -26px;
    bottom: 0;
    border: 14px solid transparent;
    border-left-color: #fff;
    border-bottom-color: #fff;
    pointer-events: none;
  }
  .chat-translate-header__tab-item.with-image:after {
    content: '';
    display: block;
    position: absolute;
    right: -26px;
    bottom: 0;
    border: 14px solid transparent;
    border-left-color: #f9f9fc;
    border-bottom-color: #f9f9fc;
  }
  .chat-translate-header__tab-item.active.with-image:before {
    content: '';
    display: block;
    position: absolute;
    left: -26px;
    bottom: 0;
    border: 14px solid transparent;
    border-right-color: #fff;
    border-bottom-color: #fff;
    pointer-events: none;
  }
  .chat-translate-header__tab-item.active.with-image:after {
    content: '';
    display: block;
    position: absolute;
    right: -26px;
    bottom: 0;
    border: 14px solid transparent;
    border-left-color: #fff;
    border-bottom-color: #fff;
  }
  .translate_box__container {
    width: 100%;
    height: 100%;
    display: -webkit-flex;
    display: flex;
    position: relative;
    min-width: 1080px;
  }
  .translate_box__container.hide {
    display: none;
    width: 0;
    height: 0;
  }
  .translate_box .translate_box_item {
    flex: 0 1 50%;
    height: 100%;
    background-color: #fff;
  }
  .translate_box .translate_box_content {
    border-radius: 0 12px 12px;
  }
  .translate_box .translate_box_result {
    border-radius: 12px;
    margin-left: 30px;
  }

  .translate_content_box {
    height: 100%;
  }
  .translate_content_header {
    padding: 17px 30px 0;
    position: relative;
    z-index: 2;
  }
  .select_language_tab {
    display: -webkit-flex;
    display: flex;
  }
  .select_language_tab .select_language_box {
    width: max-content;
    background: #ffffff;
    border-radius: 6px;
    opacity: 1;
    border: 1px solid #d9d9d9;
    cursor: pointer;
    position: relative;
  }
  .select_language_tab .select_language_box .select_language_btn {
    padding: 7px 23px;
    height: 32px;
    display: flex;
    box-sizing: border-box;
    position: relative;
  }
  .select_language_tab .select_language_box .select_language_text {
    line-height: 18px;
    font-size: 13px;
    color: #141414;
  }
  .select_language_tab .select_language_box .select_language_arrow {
    margin-left: 8px;
    line-height: 18px;
  }
  .select_language_tab .translagte_content_tab {
    margin: 0 16px;
    line-height: 32px;
  }
  .translate_content_body {
    box-sizing: content-box;
    height: calc(100% - 133px);
    padding: 17px 30px 0;
  }
  .translate_content_body .el-textarea,
  :deep .translate_content_body textarea {
    height: 100%;
    outline: none;
    resize: none;
  }

  .translate_content_footer {
    height: 35px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: right;
    justify-content: right;
    box-sizing: content-box;
    padding: 16px 30px;
    position: relative;
    align-items: center;
  }
  .translate_content_footer .nums,
  .translate_content_footer .info {
    padding-top: 3px;
    box-sizing: content-box;
    font-size: 12px;
    color: #bfbfbf;
    line-height: 24px;
  }
  .translate_content_footer .btns_item {
    width: 24px;
    height: 24px;
    margin-left: 10px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;

    align-items: center;
    margin-top: 2px;
    border-radius: 4px;
    opacity: 0.7;
    cursor: pointer;
  }
  .translate_content_footer button {
    margin-left: 12px;
  }

  .select_language_tab .select_language_box .select_language_section {
    width: 588px;
    box-shadow: 0 4px 20px 1px #1a1a1d1f;
    border-radius: 6px;
    border: 1px solid #f0f0f0;
    position: absolute;
    left: 0;
    top: 38px;
    background: #fff;
  }
  .select_language_tab .select_language_box .select_language_section .search {
    height: 42px;
    padding-top: 8px;
    box-sizing: border-box;
    margin: 0 20px;
    border-bottom: 1px solid #f0f0f0;
  }
  .select_language_tab
    .select_language_box
    .select_language_section
    .language_list {
    padding: 10px 30px;
    box-sizing: content-box;
    max-height: 260px;
    overflow: auto;
  }
  .select_language_tab
    .select_language_box
    .select_language_section
    .language_list
    .language_item {
    line-height: 38px;
    width: 25%;
    float: left;
    cursor: pointer;
    font-size: 13px;
    color: #141414;
  }
  :deep
    .select_language_tab
    .select_language_box
    .select_language_section
    .search
    .el-input__inner {
    border: none;
    box-shadow: none;
    height: 31px;
    width: 100%;
  }

  :deep
    .select_language_tab
    .select_language_box
    .select_language_section
    .search
    .el-input__icon {
    line-height: 1.5;
  }

  .translate_result_box {
    height: 100%;
    position: relative;
  }
  .translate_result_header {
    padding: 16px 30px 0;
  }
  .translate_result_title {
    height: 34px;
    line-height: 34px;
    font-size: 14px;
    color: #141414;
    font-weight: 700;
    padding-left: 9px;
    position: relative;
    display: -webkit-flex;
    display: flex;
  }
  .translate_result_title .title_icon {
    position: absolute;
    width: 5px;
    height: 12px;
    top: 11px;
    left: 0px;
    overflow: hidden;
  }
  .translate_result_title .title_icon i {
    width: 10px;
    height: 12px;
    position: absolute;
    top: 0;
    right: 0;
    background-color: #9694f2;
    border-radius: 50%;
  }
  .translate_result_title .title_text {
    margin-right: 4px;
  }
  .translate_result_title .tab_power {
    position: absolute;
    top: 0;
    right: 0;
  }
  .tab-power_box {
    height: 34px;
    padding: 4px 4px 4px 0;
    box-sizing: border-box;
    background: #f7f7f7;
    border-radius: 6px;
    display: -webkit-flex;
    display: flex;
    font-size: 13px;
    font-weight: 400;
    position: relative;
  }

  .tab-power_box .tab-power-item {
    height: 26px;
    line-height: 26px;
    padding: 0 12px;
    cursor: pointer;
    border-radius: 2px;
    color: #595959;
    margin-left: 4px;
  }

  .tab-power_box .tab-power-item.active {
    background-color: #fff;
    box-shadow: 0 2px 6px 1px #1a1a1d14;
    color: #6965ea;
  }

  .tab-power_box .tab-power-item .lock_icon {
    margin-left: 6px;
    width: 12px;
    height: 12px;
  }
  .translate_result_body {
    padding: 16px 0;
    padding-bottom: 36px;
    margin: 0 30px;
    box-sizing: border-box;
    height: calc(100% - 302px);
    border-bottom: 1px solid #f5f5f5;
    position: relative;
  }

  .translate_result_body img {
    position: absolute;
    left: 2px;
    top: 22px;
    height: 16px;
    width: 16px;
  }

  :deep .translate_result_body .el-textarea {
    height: 100%;
  }
  :deep .translate_result_body textarea {
    height: calc(100% - 24px);
    outline: none;
    resize: none;
    border: none;
    text-indent: 8px;
  }

  .translate_result_body .footerInfo {
    height: 33px;
    display: -webkit-flex;
    display: flex;

    align-items: center;
    -webkit-justify-content: right;
    justify-content: right;
  }
  .translate_result_body .footerInfo .nums {
    font-size: 12px;
    color: #bfbfbf;
    line-height: 24px;
  }
  .translate_result_body .footerInfo .btn {
    width: 24px;
    height: 24px;
    margin-left: 12px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: center;
    justify-content: center;

    align-items: center;
    border-radius: 4px;
    opacity: 0.7;
    font-size: 12px;
    cursor: pointer;
  }

  .translate_result_footer {
    padding: 20px 30px;
  }
  .translate_result_title {
    height: 34px;
    line-height: 34px;
    font-size: 14px;
    color: #141414;
    font-weight: 700;
    padding-left: 9px;
    position: relative;
    display: -webkit-flex;
    display: flex;
  }
  .translate_result_title .title_icon {
    position: absolute;
    width: 5px;
    height: 12px;
    top: 11px;
    left: 0px;
    overflow: hidden;
  }

  .translate_result_footer .translate_quest_buttons {
    margin-top: 9px;
  }
  .quest_buttons_box {
    padding: 0 2px;
    display: flex;
  }
  .quest_buttons_box .quest_list {
    max-width: calc(100% - 46px);
  }
  .quest_buttons_box .quest_list .quest_buttons_item {
    height: 28px;
    background: #f6f5fd;
    border-radius: 14px;
    padding: 0 12px;
    line-height: 28px;
    font-size: 12px;
    color: #595959;
    margin-right: 8px;
    position: relative;
    cursor: pointer;
  }

  .quest_buttons_box .quest_button_select_content {
    position: absolute;
    left: 0;
    bottom: 36px;
    width: 100%;
    min-width: 84px;
    background: #ffffff;
    box-shadow: 0 4px 12px 1px #1a1a1d1a;
    border-radius: 8px;
    border: 1px solid #f0f0f0;
    padding-bottom: 5px;
    font-size: 12px;
  }
  .quest_buttons_box .quest_button_select_content .title {
    height: 33px;
    line-height: 33px;
    color: #bfbfbf;
    padding-left: 12px;
  }
  .quest_buttons_box .quest_button_select_content .select_item {
    margin: 0 5px;
    height: 30px;
    line-height: 30px;
    color: #141414;
    padding-left: 12px;
    border-radius: 4px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .quest_buttons_box .quest_button_select_content .select_item:hover {
    background: #f0effd;
  }

  .translate_result_footer .input_area {
    position: relative;
  }

  :deep .translate_result_footer .input_area textarea {
    margin-top: 10px;
    height: 132px;
    resize: none;
    border: 1px solid #dedede;
    border-radius: 8px;
  }

  .translate_result_footer .input_area .button_publish {
    width: 36px;
    height: 30px;
    background: #6965ea;
    border-radius: 6px;
    position: absolute;
    bottom: 8px;
    right: 8px;
    color: #fff;
    font-size: 18px;
    line-height: 30px;
    text-align: center;
    cursor: pointer;
  }

  .translate-img-box__language-box {
    padding: 17px 30px 0;
    position: relative;
    z-index: 4;
  }
  .translate-img-box__img-box {
    box-sizing: content-box;
    height: calc(100% - 133px);
    padding: 17px 30px 0;
  }
  .translate-img-upload {
    width: 100%;
    height: 100%;
    position: relative;
    display: -webkit-flex;
    display: flex;

    align-items: center;
  }
  .translate-img-upload__init {
    width: 100%;
    height: -moz-max-content;
    height: -webkit-max-content;
    height: max-content;
    box-sizing: border-box;
    overflow: hidden;
    position: relative;
    cursor: pointer;
    border-radius: 4px;
    padding: 20px;
    border: 1px solid transparent;
  }
  .translate-img-upload__init__upload-icon {
    width: 60px;
    height: 60px;
    display: block;
    margin: 0 auto;
  }
  .translate-img-upload__init__upload-title {
    font-size: 14px;
    height: 20px;
    width: 100%;
    font-family: '\82f9\65b9-\7b80-\5e38\89c4\4f53, \82f9\65b9-\7b80';
    color: #595959;
    line-height: 20px;
    text-align: center;
    margin-top: 0;
  }
  .translate-img-upload__init__upload-tips {
    font-size: 13px;
    width: 100%;
    height: 20px;
    font-family: '\82f9\65b9-\7b80-\5e38\89c4\4f53, \82f9\65b9-\7b80';
    color: #a1a1a1;
    line-height: 20px;
    text-align: center;
    margin-top: 0;
  }
  .translate-img-upload__init__upload-tips.des {
    margin-top: 10px;
    font-size: 14px;
    color: #6965ea;
  }

  .translate-img-box__left-bottom-box {
    height: 35px;
    display: -webkit-flex;
    display: flex;
    -webkit-justify-content: right;
    justify-content: right;

    align-items: center;
    box-sizing: content-box;
    padding: 16px 30px;
    position: relative;
  }
  .translate-img-box__start-btn:disabled {
    background: #bfbfbf;
    color: #fff;
    border: none;
  }
  .translate-img-box__guide {
    width: 100%;
    height: 100%;
    padding: 30px;
  }
  .translate-img-box__guide__title {
    width: 100%;
    height: 34px;
    line-height: 34px;
    font-size: 14px;
    color: #141414;
    font-weight: 700;
    padding-left: 9px;
    position: relative;
    display: -webkit-flex;
    display: flex;
    margin-bottom: 23px;
  }
  .translate-img-box__guide__title-icon {
    position: absolute;
    width: 5px;
    height: 12px;
    top: 11px;
    left: 0px;
    overflow: hidden;
  }
  .translate-img-box__guide__title-icon i {
    width: 10px;
    height: 12px;
    position: absolute;
    top: 0;
    right: 0;
    background-color: #9694f2;
    border-radius: 50%;
  }
  .translate-img-box__guide__title-text {
    margin-right: 4px;
  }
  .translate-img-box__guide__tips {
    width: 100%;
    font-size: 14px;
    font-family: '\82f9\65b9-\7b80-\5e38\89c4\4f53, \82f9\65b9-\7b80';
    font-weight: 400;
    color: #141414;
    line-height: 24px;
  }
  .translate-img-box__guide__tips-item {
    width: 50%;
    float: left;
    font-size: 14px;
    font-family: '\82f9\65b9-\7b80-\5e38\89c4\4f53, \82f9\65b9-\7b80';
    color: #141414;
    line-height: 24px;
  }
  .translate-img-box__guide__tips-title {
    margin: 30px 0 8px;
  }
  .translate-img-box__guide__tips-img {
    width: calc(100% - 60px);
  }
  .translate-img-box__guide:after {
    content: '';
    display: block;
    clear: both;
  }
</style>
