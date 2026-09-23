<template>
  <div class="container">
    <div style="flex: 1; max-width: 800px; margin: 0 auto">
      <div class="chat-content">
        <div class="smart-model" v-if="smartModelVisible">
          <div class="card-item">
            <div class="card-icon"><img :src="msgData.imgUrl" alt="" /></div>
            <div class="right">
              <div class="card-title">{{ msgData.label }}</div>
              <div class="card-status">
                <img
                  src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTcuNjYwNCAxNC4wMDAyQzYuNzgzODQgMTQuMDAwMiA2LjAxODI0IDEzLjg0MyA1LjM2MzYgMTMuNTI4NkM0LjcxMjY1IDEzLjIxNDIgNC4yMDU5NSAxMi43NzQxIDMuODQzNDkgMTIuMjA4MkMzLjQ4MTAzIDExLjY0MjMgMy4yOTk4IDEwLjk4MDMgMy4yOTk4IDEwLjIyMjFDMy4yOTk4IDkuODIyNjIgMy4zMzEyNCA5LjQ2NzU2IDMuMzk0MTIgOS4xNTY4OEMzLjQ2MDY5IDguODQ2MiAzLjU0MDIxIDguNTY2OTYgMy42MzI2NyA4LjMxOTE1QzMuNzI4ODQgOC4wNjc2NSAzLjgyNSA3LjgzMDk0IDMuOTIxMTYgNy42MDkwM0M0LjAxNzMyIDcuMzg3MTIgNC4wOTY4NCA3LjE2NzA1IDQuMTU5NzIgNi45NDg4M0M0LjIyMjU5IDYuNzMwNjIgNC4yNTQwMyA2LjQ5NzYxIDQuMjU0MDMgNi4yNDk4MUM0LjI1NDAzIDYuMTI3NzUgNC4yNDY2NCA1Ljk5NDYgNC4yMzE4NCA1Ljg1MDM2QzQuMjIwNzUgNS43MDI0MiA0LjIxNTIgNS41OTMzMSA0LjIxNTIgNS41MjMwNEM0LjIxNTIgNS40MDA5OCA0LjI1MDMzIDUuMzAxMTIgNC4zMjA2MSA1LjIyMzQ1QzQuMzkwODggNS4xNDU3OCA0LjQ4MzM0IDUuMTA2OTUgNC41OTggNS4xMDY5NUM0Ljc5NDAyIDUuMTA2OTUgNC45OTE4OSA1LjE2NjEzIDUuMTkxNjIgNS4yODQ0OEM1LjM5NTA0IDUuNDAyODMgNS41NzYyNyA1LjU2NzQyIDUuNzM1MyA1Ljc3ODI0QzUuODk4MDQgNS45ODUzNiA2LjAyMTk0IDYuMjI1NzcgNi4xMDcwMSA2LjQ5OTQ2TDUuODI5NjIgNi41NTQ5NEM1LjkxODM4IDYuMzk5NiA1Ljk3Mzg2IDYuMjYyNzUgNS45OTYwNSA2LjE0NDRDNi4wMjE5NCA2LjAyMjM0IDYuMDM0ODkgNS44OTg0NCA2LjAzNDg5IDUuNzcyNjlDNi4wMzExOSA1LjQwNjUzIDUuOTYyNzYgNS4wNTg4NyA1LjgyOTYyIDQuNzI5NjlDNS43MDAxNyA0LjQwMDUyIDUuNTMzNzMgNC4wODc5OSA1LjMzMDMxIDMuNzkyMTFDNS4xMzA1OSAzLjQ5NjIyIDQuOTIxNjIgMy4yMTY5OCA0LjcwMzQxIDIuOTU0MzhDNC42NDQyMyAyLjg4NzgxIDQuNTk5ODUgMi44MTkzOSA0LjU3MDI2IDIuNzQ5MTFDNC41NDA2NyAyLjY3ODg0IDQuNTI1ODggMi42MDg1NyA0LjUyNTg4IDIuNTM4MjlDNC41MjU4OCAyLjM2MDc2IDQuNjAxNyAyLjIyNzYxIDQuNzUzMzQgMi4xMzg4NUM0LjkwODY4IDIuMDQ2MzggNS4xMTc2NCAyLjAwMDE1IDUuMzgwMjQgMi4wMDAxNUM1Ljg2ODQ1IDIuMDAwMTUgNi4zODI1NSAyLjA1OTMzIDYuOTIyNTQgMi4xNzc2OEM3LjQ2NjIzIDIuMjkyMzQgOC4wMDQzNyAyLjQ3MzU3IDguNTM2OTYgMi43MjEzN0M5LjA3MzI1IDIuOTY1NDggOS41ODE4IDMuMjc4MDEgMTAuMDYyNiAzLjY1ODk2QzEwLjU0MzQgNC4wMzYyMSAxMC45Njg4IDQuNDg1NTkgMTEuMzM4NiA1LjAwNzA5QzExLjcxMjIgNS41MjQ4OSAxMi4wMDYyIDYuMTE4NTEgMTIuMjIwNyA2Ljc4Nzk1QzEyLjQzNTIgNy40NTczOSAxMi41NDI1IDguMjA2MzUgMTIuNTQyNSA5LjAzNDgzQzEyLjU0MjUgOS43NzgyNCAxMi40MjYgMTAuNDU1MSAxMi4xOTMgMTEuMDY1M0MxMS45NjM3IDExLjY3NTYgMTEuNjMyNyAxMi4xOTkgMTEuMTk5OSAxMi42MzU0QzEwLjc2NzIgMTMuMDcxOCAxMC4yNTEyIDEzLjQwODQgOS42NTIwNyAxMy42NDUxQzkuMDU2NjEgMTMuODgxOCA4LjM5MjcyIDE0LjAwMDIgNy42NjA0IDE0LjAwMDJaTTcuNzg4IDEyLjQ0MTJDOC4yMjgxMyAxMi40NDEyIDguNTkyNDQgMTIuMzQ1IDguODgwOTMgMTIuMTUyN0M5LjE3MzExIDExLjk2MDQgOS4zOTEzMyAxMS43MDcgOS41MzU1NyAxMS4zOTI3QzkuNjc5ODEgMTEuMDc4MyA5Ljc1MTk0IDEwLjczOCA5Ljc1MTk0IDEwLjM3MTlDOS43NTE5NCAxMC4wMDk0IDkuNjgxNjYgOS42NDUwOSA5LjU0MTEyIDkuMjc4OTNDOS40MDQyNyA4LjkxMjc3IDkuMTk5IDguNTc0MzYgOC45MjUzMSA4LjI2MzY4QzguNjUxNjIgNy45NTMgOC4zMTEzNSA3LjcwMTQ5IDcuOTA0NTEgNy41MDkxN0M3Ljg3ODYyIDcuNDk4MDcgNy44NTgyNyA3LjQ5OTkyIDcuODQzNDggNy41MTQ3MkM3LjgyODY5IDcuNTI1ODEgNy44MjMxNCA3LjU0NDMgNy44MjY4NCA3LjU3MDE5QzcuODc0OTIgOC4wMjUxMiA3Ljg2NzUyIDguNDQ4NiA3LjgwNDY1IDguODQwNjVDNy43NDE3NyA5LjIyOSA3LjYzNjM2IDkuNTIzMDQgNy40ODg0MiA5LjcyMjc2QzcuNDE4MTUgOS41NjM3MiA3LjMzODYzIDkuNDE1NzggNy4yNDk4NiA5LjI3ODkzQzcuMTY0OCA5LjEzODM5IDcuMDU3NTQgOS4wMTA3OSA2LjkyODA5IDguODk2MTNDNi45MDk2IDguODgxMzQgNi44OTExIDguODc3NjQgNi44NzI2MSA4Ljg4NTAzQzYuODU3ODIgOC44ODg3MyA2Ljg0ODU3IDguOTAzNTMgNi44NDQ4NyA4LjkyOTQyQzYuODIyNjggOS4wODg0NiA2Ljc3MDkgOS4yMzQ1NSA2LjY4OTUzIDkuMzY3N0M2LjYwODE2IDkuNDk3MTUgNi41MTk0IDkuNjMzOTkgNi40MjMyNCA5Ljc3ODI0QzYuMzI3MDcgOS45MTg3OCA2LjI0MjAxIDEwLjA3NzggNi4xNjgwMyAxMC4yNTU0QzYuMDk3NzYgMTAuNDI5MiA2LjA2MjYzIDEwLjYzODIgNi4wNjI2MyAxMC44ODIzQzYuMDYyNjMgMTEuMzQ4MyA2LjIxOTgxIDExLjcyNTUgNi41MzQxOSAxMi4wMTRDNi44NTIyNyAxMi4yOTg4IDcuMjcwMiAxMi40NDEyIDcuNzg4IDEyLjQ0MTJaIiBmaWxsPSIjOTk5OTk5Ii8+Cjwvc3ZnPgo="
                  alt=""
                />
                <div>{{ msgData.status }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="line" v-for="(item, index) in dialogue" :key="index">
          <div class="chat-item question" v-if="item.role === 'user'">
            {{ item.content }}
          </div>
          <div class="chat-item answer" v-if="item.role === 'assistant'">
            <div
              class="assistant-avatar"
              style="margin-bottom: 10px; display: flex; align-items: center"
            >
              <img
                src="@/assets/deepseek.svg"
                style="width: 30px; height: 30px"
                alt=""
              />
              问心AI：
              <div
                v-if="item.think"
                style="
                  padding: 4px 14px;
                  background: rgb(237 237 237);
                  margin-left: 10px;
                  border-radius: 10px;
                  font-size: 14px;
                "
              >
                <i class="icon el-icon-check"></i>
                已深度思考
              </div>
            </div>
            <div
              v-if="item.think && item.thinkContent"
              style="
                border-left: 2px solid rgba(0, 0, 0, 0.1);
                padding-left: 8px;
                margin-bottom: 30px;
                color: #8b8b8b;
                font-size: 14px;
                margin-left: 40px;
              "
            >
              {{ item.thinkContent }}
            </div>
            <div style="margin-left: 40px; white-space: pre-wrap;">
              {{ item.content }}
            </div>

            <!-- 多轮思考：显示额外的思考内容 -->
            <template v-if="item.extraBlocks && item.extraBlocks.length > 0">
              <template v-for="(block, blockIndex) in item.extraBlocks">
                <!-- 额外的思考内容 -->
                <div
                  v-if="block.type === 'thinking' && block.content"
                  :key="'think-' + blockIndex"
                  style="
                    border-left: 2px solid rgba(0, 0, 0, 0.1);
                    padding-left: 8px;
                    margin-top: 20px;
                    margin-bottom: 10px;
                    color: #8b8b8b;
                    font-size: 14px;
                    margin-left: 40px;
                  "
                >
                  <div class="think-header" style="display: flex; align-items: center; margin-bottom: 8px;">
                    <i class="icon el-icon-loading" v-if="block.isThinking"></i>
                    <i class="icon el-icon-check" v-else></i>
                    {{ block.isThinking ? '正在思考...' : '继续思考' }}
                  </div>
                  {{ block.content }}
                </div>
              </template>
            </template>
          </div>
          <div
            class="answer-action"
            v-if="index === dialogue.length - 1 && item.role === 'assistant'"
          >
            <div class="action-btn">
              <i class="icon el-icon-phone-outline"></i>
              <i class="icon el-icon-document-copy"></i>
              <i class="icon el-icon-refresh"></i>
              <i class="icon el-icon-share">
                <span style="margin-left: 4px; font-size: 12px">分享</span>
              </i>
              <i class="icon el-icon-more"></i>
            </div>
            <div class="realtive-msg">
              <div
                class="msg-item"
                v-for="item in realtiveQuestion"
                :key="item.msg"
              >
                {{ item.msg }}
                <i class="icon el-icon-right"></i>
              </div>
            </div>
          </div>
        </div>

        <Loading v-if="isLoading" loadintText="思考中" />
      </div>

      <div class="search-input">
        <div
          class="result-btn"
          @click="detailVisiabled = !detailVisiabled"
          v-show="!detailVisiabled"
        >
          查看结果
        </div>
        <div class="relative-list">
          <div class="list-item" v-for="item in relativeList" :key="item.title">
            <el-button round class="icon-btn">
              <i
                :class="`icon el-icon-${item.icon}`"
                :style="{ color: item.color }"
              ></i>
              <span class="btn-title">{{ item.title }}</span>
            </el-button>
          </div>
        </div>
        <div class="input-content">
          <el-input
            class="input-style"
            v-model="searchKey"
            clearable
            placeholder="发消息、输入 @ 或 / 选择技能"
            @keydown.enter.native="onSendMessage"
          />
          <div class="btn-group">
            <div
              @click="think = !think"
              class="deep"
              :style="`${
                think
                  ? 'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);'
                  : ''
              }`"
            >
              深度思考
              <i
                v-if="!think"
                class="icon el-icon-turn-off"
                style="font-size: 20px; margin-right: 0; margin-left: 4px"
              ></i>
              <i
                v-else
                class="icon el-icon-open"
                style="font-size: 20px; margin-right: 0; margin-left: 4px"
              ></i>
            </div>
            <div
              @click="search = !search"
              class="deep"
              :style="`${
                search
                  ? 'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);'
                  : ''
              }`"
            >
              联网
              <i
                v-if="!search"
                class="icon el-icon-turn-off"
                style="font-size: 20px; margin-right: 0; margin-left: 4px"
              ></i>
              <i
                v-else
                class="icon el-icon-open"
                style="font-size: 20px; margin-right: 0; margin-left: 4px"
              ></i>
            </div>

            <!-- <i class="icon el-icon-paperclip"></i> -->
            <!-- <i class="icon el-icon-picture"></i>
            <i class="icon el-icon-scissors"></i> -->
            <fileUpload ref="fileUpload" v-model="fileObject" />
            <voice class="icon" @getMessage="getMessage" />
            <i
              v-if="!stopLoading"
              class="icon el-icon-video-pause send"
              @click="stopTypeing"
            ></i>
            <i v-else class="icon el-icon-top send" @click="onSendMessage"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="right-content" v-if="detailVisiabled">
      <div class="top-nav">
        <i class="icon el-icon-close" @click="detailVisiabled = false"></i>
        <div class="title">beijing_school_district_report.html</div>
        <el-button type="info" size="mini">预览</el-button>
        <el-button class="btn" type="default" size="mini">代码</el-button>
        <img class="icon" src="./icon/fullscreen.svg" alt="" />
        <i class="icon el-icon-download"></i>
      </div>
      <div class="file-content">
        Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dolorem quo
        maiores aliquam incidunt temporibus, non nostrum deserunt expedita.
        Error, nihil eum exercitationem libero eveniet in provident officiis
        repellendus eaque fugiat! Pariatur repellat facilis fugiat, incidunt
        dolore illum nisi. Deleniti eveniet ex corrupti qui fugiat, libero ea
        repellat, culpa expedita laborum sint et facere laboriosam vel iste non
        sit dolorem cum! Delectus, quia! Eaque consectetur vitae, reiciendis
        neque officia a magnam qui vero eius ut fugit voluptatem cum veniam
        nobis natus necessitatibus adipisci deserunt, blanditiis esse?
        Consequatur voluptatem tenetur nisi commodi. Commodi ipsum a quos!
        Impedit non at sint iusto voluptas labore, facere pariatur qui quas
        dolor magni inventore facilis aut ex aspernatur eligendi omnis doloribus
        dolores numquam quidem excepturi ut. Neque dignissimos minus, itaque
        deleniti voluptatibus voluptate maxime repellendus tempore a non,
        aperiam, eveniet omnis qui numquam optio dolores architecto? Est
        aspernatur delectus ullam excepturi ea repellendus aut eligendi neque?
        Pariatur dignissimos quisquam minima molestias commodi quos vitae
        facilis libero, repellat ut corrupti quae debitis odio architecto? Minus
        labore ipsam aliquam deserunt suscipit obcaecati? Excepturi optio
        delectus consequatur voluptatibus recusandae? Incidunt quo pariatur
        reprehenderit sit illum, labore optio enim minima architecto, quis
        reiciendis eum? Autem velit molestiae ullam, nam perspiciatis ex
        adipisci, at hic, modi magni quod neque nemo reiciendis. Voluptates
        dolor, doloribus perferendis magni iste aspernatur. Tempora ducimus esse
        fuga ipsam recusandae quos? Id nesciunt, minima eveniet deleniti
        inventore ut possimus repellat at eaque culpa officia consectetur
        temporibus consequuntur? Suscipit sunt quidem fuga est veritatis
        officiis delectus nemo non cum minus quis esse quia accusantium dolorum
        quos ipsam vero nam corporis quibusdam, vitae facere maxime. Alias
        mollitia eligendi illo. Dicta minima modi excepturi asperiores quaerat
        voluptatibus, iusto odio animi eligendi, iste quis quidem repudiandae
        eaque ab, architecto voluptatem? Provident nulla soluta quasi officia
        est distinctio tenetur ab cupiditate commodi. Lorem ipsum dolor sit amet
        consectetur adipisicing elit. Facilis recusandae cupiditate nihil
        accusantium vitae voluptatem perspiciatis, eveniet vel architecto libero
        nesciunt modi cum, deleniti ad ducimus sed laborum amet. Quo? Lorem,
        ipsum dolor sit amet consectetur adipisicing elit. Modi provident unde
        corporis inventore pariatur, distinctio ut vel rerum, necessitatibus
        maiores nam ad ducimus error amet eveniet nesciunt vitae repellendus
        est. Molestias corporis cumque nesciunt ab at ad facere officiis quaerat
        suscipit aut, culpa repellendus quas nihil atque saepe velit asperiores
        aliquid praesentium nobis corrupti rerum in perspiciatis consectetur.
        Necessitatibus, incidunt! Nobis voluptates maiores dolorum dolor nihil
        quae consectetur? Voluptas illum ex optio expedita beatae? Suscipit sed
        maiores eos officia optio. Beatae ipsam animi aspernatur illum ad in
        laudantium aliquam doloribus. Debitis, explicabo magnam officiis quasi
        esse harum architecto sunt suscipit qui officia cum voluptate
        necessitatibus cumque nihil illum aspernatur perferendis quae ipsam
        quaerat cupiditate saepe! Laboriosam nisi accusantium dolores laborum.
        Cum nam deleniti assumenda. Vel expedita esse corporis cum dignissimos,
        quia veritatis? Nisi perferendis officia ullam, quo dolore quaerat alias
        praesentium temporibus omnis aliquid a voluptatum repellat, illo enim
        consequatur? Saepe, nulla dolore delectus eligendi, numquam ut quaerat
        aliquid nesciunt, consequatur accusamus voluptatum. Quo rem aliquam
        vitae dolorum voluptate autem, velit repellendus sapiente repudiandae
        quas quibusdam et ullam, dicta labore. Minus nam consequatur nihil
        ducimus facilis incidunt asperiores, ut rem. Blanditiis adipisci neque
        aliquam quos quidem officia hic nam reiciendis labore voluptatum.
        Reiciendis neque quibusdam harum recusandae commodi quasi facilis. Natus
        ipsa facilis, molestiae soluta dolores, ex cupiditate ullam doloribus
        sapiente, officiis aliquid quisquam recusandae quis libero. Placeat
        velit odit voluptatibus eum blanditiis similique ex aliquid quisquam!
        Voluptas, doloremque ut. Praesentium beatae quas ipsa rem ullam possimus
        quasi officia ratione quis quidem voluptate tempore sunt provident
        adipisci, sit hic inventore id perferendis repellat ab? Dolorem ipsum
        cum laborum deserunt recusandae. Atque magnam a nisi omnis corrupti.
        Expedita cum odit accusamus maiores, unde quam in quasi possimus impedit
        illum repudiandae at voluptatum soluta ratione quas sapiente!
        Accusantium labore minus omnis quo. Fuga dolores minus consequatur
        voluptatibus deleniti. Unde numquam nemo eveniet sunt rerum fugit,
        similique iure architecto odio deleniti quam et inventore vitae,
        tenetur, corporis fugiat aspernatur distinctio quisquam ad.
        Voluptatibus! Ducimus libero labore voluptate? Temporibus dicta officiis
        magnam dolores a veniam obcaecati expedita quidem ducimus ad, numquam
        nobis cum illum quaerat debitis! Qui dicta quibusdam, nobis atque amet
        dolorem facere. Vero quam explicabo dolor eos delectus. Perspiciatis,
        nobis culpa numquam minima voluptatum temporibus cupiditate iure,
        dignissimos dolor doloremque ipsa tempore nam voluptas ratione cum,
        asperiores eos vero ea dolorem molestiae? Beatae sequi eius nemo
        accusantium similique suscipit commodi, id sapiente ab. Voluptas
        assumenda nihil, ipsa vero animi deleniti quam, ducimus consequuntur
        rerum perspiciatis magni, dolores tenetur numquam impedit veniam
        mollitia? Laborum veritatis dolor odit, dolores beatae quidem provident
        fugiat explicabo repellendus at voluptas facilis ipsa eligendi velit
        quam doloribus aliquam qui nobis molestiae, voluptatem architecto
        inventore illum ad. Optio, autem! Illum quisquam tempore eligendi
        blanditiis id dolor totam? Amet voluptate assumenda vero beatae officia
        tempora voluptas? Ut aperiam, ducimus modi tempore neque optio officia,
        rem eaque porro quod vitae similique? Quos dignissimos, quaerat velit,
        sint mollitia quod maxime dolore commodi veniam voluptates distinctio
        excepturi magnam et? Quos corrupti dicta, nisi totam obcaecati, quae
        aperiam quaerat quam necessitatibus pariatur dignissimos corporis! Iusto
        accusantium pariatur dolores animi delectus qui nulla corporis
        repudiandae. Asperiores eum est saepe illum delectus possimus omnis,
        obcaecati quia distinctio, facilis nostrum similique doloribus. Cumque,
        eos. Delectus, perferendis odit. Molestias ea, possimus veniam eius iste
        vitae, placeat officia ex voluptatem minus cum nisi quo laudantium
        nobis. Molestias, delectus quidem expedita praesentium accusantium, quod
        dolor dolores, nemo temporibus laboriosam atque. Porro dolorem eveniet
        perferendis, voluptates incidunt nostrum id nihil sunt perspiciatis
        explicabo, enim libero beatae consectetur voluptate ut eum ullam facere
        eaque, quo cumque illo quam? Ut molestiae ad reprehenderit?
      </div>
      <div class="footer">
        <i class="el-icon-back"></i>
        <div class="page">1</div>
        <i class="el-icon-right"></i>
      </div>
    </div>
  </div>
</template>

<script>
  import voice from './voice.vue'
  import fileUpload from './fileUpload.vue'
  // import axios from 'axios' // 旧接口,已注释
  import Loading from '@/components/Loading.vue'
  import {
    getAgentDialogueList,
    sageOrUpdateAgentDialogue,
  } from '@/api/ai/index.js'
  import { chatStream } from '@/api/ai/starlight' // 新接口

  export default {
    props: ['msgData'],
    components: { voice, Loading, fileUpload },
    data() {
      return {
        searchKey: '',
        source: '全网搜索',
        userName: '',
        detailVisiabled: false,
        relativeList: [
          {
            title: '帮我写作',
            icon: 'edit',
            color: '',
          },
          {
            title: '图像生成',
            icon: 'picture-outline',
            color: '',
          },
          {
            title: 'AI 搜索',
            icon: 'search',
            color: '',
          },
          {
            title: 'AI 阅读',
            icon: 'view',
            color: '',
          },
          {
            title: '学术搜索',
            icon: 's-cooperation',
            color: '',
          },
          {
            title: '解题答疑',
            icon: 'document-checked',
            color: '',
          },
        ],
        dialogue: [],
        realtiveQuestion: [
          { msg: 'What can you do?' },
          { msg: 'Can you tell me a joke?' },
          { msg: 'Who created you?' },
        ],
        intervalId: null, //定时器
        isLoading: false, //正在回复
        currentObj: {}, //传来的数据
        stopLoading: true,
        historyArr: [], //历史的所有数据
        smartModelVisible: false, // 智能体
        think: false, // 深度思考
        search: false, // 联网搜索
        fileObject: {}, // 文件
        sessionId: '', // 会话ID
        abortController: null, // 用于中止流式请求
        isStreaming: false, // 是否正在流式输出
      }
    },
    watch: {
      msgData: {
        deep: true,
        handler(newVal, oldVal) {
          if (JSON.stringify(newVal) !== JSON.stringify(oldVal)) {
            this.updateMsgData()
          }
        },
      },
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      this.fileObject = this.msgData?.fileObject || {}
      this.sessionId = 'session_' + Date.now() // 生成会话ID
    },
    mounted() {
      this.updateMsgData()
      if (this.msgData.msg) {
        this.searchKey = this.msgData.msg
        this.think = !!this.msgData.think
        this.search = !!this.msgData.search
        this.onSendMessage()
      }
    },
    methods: {
      updateMsgData() {
        if (this.msgData.smartModel) {
          this.dialogue = [{ role: 'assistant', content: this.msgData.desc }]
          this.realtiveQuestion = this.msgData.relativeQuestions
          this.smartModelVisible = true
        }
      },
      onSendMessage(key) {
        if (!this.searchKey) {
          this.$message.error('不能发送空白消息哦')
          return
        }
        // this.showButton = false
        this.stopLoading = false

        if (this.isLoading) {
          return
        }

        this.dialogue.push({ role: 'user', content: this.searchKey }) //保存问题对话

        this.isLoading = true
        let prompt = this.searchKey
        if (this.fileObject && this.fileObject.content) {
          prompt = this.searchKey + this.fileObject.content
        }

        // ========== 旧接口调用(已注释) ==========
        // axios({
        //   url: `https://hit-mitlab.cn:7860/generate_doc`,
        //   method: 'post',
        //   headers: { 'Content-Type': 'application/json' },
        //   data: {
        //     prompt,
        //     temperature: 1.0,
        //     max_tokens: 2048,
        //     history: this.handleData(this.dialogue),
        //     think: this.think,
        //   },
        // })
        //   .then((res) => {
        //     if (!this.stopLoading) {
        //       this.addMessage({ content: res.data.text })
        //       sageOrUpdateAgentDialogue({ name: prompt })
        //       this.$emit('fetchAgentDialogue')
        //     }
        //   })
        //   .catch((err) => {
        //     console.log('err', err)
        //     this.dialogue.push({
        //       role: 'assistant',
        //       content: '生成失败！请重试！',
        //     }) //保存接口回答的对话
        //   })
        //   .finally(() => {
        //     this.isLoading = false
        //   })
        // ========== 旧接口调用结束 ==========

        // ========== 新接口调用(chatStream) ==========
        // 添加助手消息占位
        this.dialogue.push({
          role: 'assistant',
          content: '',
          thinkContent: '',
          think: this.think,
          extraBlocks: [] // 多轮思考内容
        })

        this.isStreaming = true
        this.isLoading = true
        this.stopLoading = false

        const currentIndex = this.dialogue.length - 1

        // 调用流式接口
        this.abortController = chatStream(
          {
            message: prompt,
            sessionId: this.sessionId,
            enableThinking: this.think,
            enableWebSearch: this.search,
            stream: true,
          },
          {
            onThinking: (data) => {
              if (data.type === 'start') {
                // 思考开始
                const currentContent = this.dialogue[currentIndex].content.trim()
                if (currentContent) {
                  // 已有第一轮内容，创建新的思考块
                  this.dialogue[currentIndex].extraBlocks.push({
                    type: 'thinking',
                    content: '',
                    isThinking: true
                  })
                }
              } else if (data.type === 'content') {
                // 思考内容
                const extraBlocks = this.dialogue[currentIndex].extraBlocks
                const lastThinkingBlock = extraBlocks.filter(b => b.type === 'thinking').pop()
                if (lastThinkingBlock && lastThinkingBlock.isThinking) {
                  // 追加到额外思考块
                  lastThinkingBlock.content += data.content
                } else {
                  // 追加到第一轮思考
                  this.dialogue[currentIndex].thinkContent += data.content
                }
              } else if (data.type === 'end') {
                // 思考结束
                const extraBlocks = this.dialogue[currentIndex].extraBlocks
                const lastThinkingBlock = extraBlocks.filter(b => b.type === 'thinking').pop()
                if (lastThinkingBlock) {
                  lastThinkingBlock.isThinking = false
                }
              }
            },
            onMessage: (content) => {
              // 所有文本内容都追加到主 content
              this.dialogue[currentIndex].content += content
              this.isLoading = false
            },
            onToolCall: (tool) => {
              // 可以在这里处理工具调用
            },
            onProgress: (message) => {
              // 显示进度 - 追加到主 content
              this.dialogue[currentIndex].content += '\n' + message
            },
            onError: (error) => {
              this.isLoading = false
              this.dialogue[currentIndex].content = `请求失败: ${error}`
              this.isStreaming = false
              this.stopLoading = true
              this.$message.error(`请求失败: ${error}`)
            },
            onComplete: () => {
              this.isLoading = false
              this.isStreaming = false
              this.stopLoading = true
              // 保存对话记录
              sageOrUpdateAgentDialogue({ name: prompt })
              this.$emit('fetchAgentDialogue')
            }
          }
        )
        // ========== 新接口调用结束 ==========

        this.searchKey = ''
      },
      // 生成对话
      addMessage(message) {
        let thinkText = ''
        let contentText = ''
        if (this.think) {
          thinkText = message.content
            .split('<think>')[1]
            .split('</think>')[0]
            .trim()
          contentText = message.content.split('</think>')[1].trim()
        } else {
          contentText = message.content
        }

        this.dialogue.push({
          role: 'assistant',
          content: '',
          thinkContent: '',
          think: this.think,
        })

        if (thinkText) {
          this.typeContentMsg(contentText, thinkText)
        } else {
          this.typeContentMsg(contentText)
        }
      },
      typeContentMsg(contentText, thinkText) {
        let i = 0
        const msgLength = thinkText ? thinkText.length : contentText.length
        const typingDelay = Math.floor(Math.random() * 100) + 50 // 随机生成打字的延迟时间

        const typeNextLetter = () => {
          if (thinkText) {
            this.dialogue[this.dialogue.length - 1].thinkContent +=
              thinkText.charAt(i)
          } else {
            this.dialogue[this.dialogue.length - 1].content +=
              contentText.charAt(i)
          }
          i++
          if (i <= msgLength) {
            //点击停止按钮，清除定时器
            if (!this.stopLoading) {
              setTimeout(typeNextLetter, typingDelay)
            } else {
              this.clearT()
            }
          } else {
            if (thinkText) {
              this.typeContentMsg(contentText)
            } else {
              this.clearT()
            }
          }
        }
        this.intervalId = setTimeout(typeNextLetter, 500)
      },
      clearT() {
        if (this.intervalId) {
          this.isLoading = false
          this.stopLoading = true
          clearTimeout(this.intervalId)
          this.intervalId = null
        }
      },
      stopTypeing() {
        this.stopLoading = true
        // 停止流式输出
        if (this.abortController) {
          this.abortController.abort()
          this.abortController = null
        }
        this.isStreaming = false
        this.isLoading = false
      },
      handleData(data) {
        const info = data.slice(0, data.length - 1).reverse()

        let accumulatedLength = 0
        const arr = []

        for (const item of info) {
          accumulatedLength += item.content.length
          if (accumulatedLength <= 4000) {
            arr.push(item)
          } else {
            break
          }
        }

        return arr.slice().reverse()
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
    },
  }
</script>

<style scoped>
  .container {
    display: flex;
    padding: 12px;
    overflow: hidden;
  }

  .right-content {
    flex: 1;
    /* border: 1px solid #000; */
    border-radius: 16px;
    background: #fff;
    box-shadow: rgba(0, 8, 24, 0.12) 1px 3px 28.8px;
    padding: 16px;
    overflow: hidden;
  }

  .right-content .top-nav {
    display: flex;
    align-items: center;
    width: 100%;
    height: 50px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    margin-bottom: 10px;
  }

  .right-content .title {
    flex: 1;
  }

  .right-content .icon {
    width: 24px;
    height: 24px;
    font-size: 24px;
    cursor: pointer;
    margin-right: 6px;
  }

  .right-content .btn {
    margin-right: 12px;
  }

  .right-content .file-content {
    max-width: 800px;
    margin: 0px auto;
    padding: 24px 40px 50px 40px;
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    height: 88%;
    overflow: auto;
  }

  .right-content .footer {
    height: 50px;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: 10px;
    font-size: 20px;
    display: flex;
    align-items: center;
  }

  .right-content .page {
    margin: 0 16px;
  }

  .chat-content {
    /* width: 810px; */
    position: relative;
    flex: 1;
    height: calc(100% - 140px);
    overflow: auto;
    display: flex;
    flex-direction: column;
    padding: 50px 0;
    margin: 0 auto;
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
  }

  .chat-content .line {
    margin-bottom: 40px;
    padding-right: 10px;
  }

  .chat-item {
    white-space: pre-wrap;
  }

  .chat-content .question {
    justify-self: flex-end;
    padding: 9px 16px;
    border-radius: 12px;
    font-size: 16px;
    white-space: normal;
    word-wrap: normal;
    background: rgba(0, 0, 0, 0.04);
    color: rgba(0, 0, 0, 0.85);
    max-width: 450px;
    width: fit-content;
  }

  .chat-content .answer {
    justify-self: flex-start;
    color: rgba(0, 0, 0, 0.85);
    font-size: 16px;
    line-height: 24px;
    cursor: pointer;
  }

  .chat-content .answer-action {
    min-height: 40px;
  }

  .action-btn .icon {
    font-size: 16px;
    margin: 20px 20px 20px 0;
    cursor: pointer;
  }

  .realtive-msg .msg-item {
    padding: 9px 16px;
    border-radius: 12px;
    font-size: 14px;
    white-space: normal;
    word-wrap: normal;
    background: rgba(0, 0, 0, 0.04);
    color: rgba(0, 0, 0, 0.85);
    max-width: 450px;
    width: fit-content;
    margin-bottom: 12px;
    cursor: pointer;
  }

  .menu-item {
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
  }

  .menu-icon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
  }

  .search-input {
    max-width: 90%;
    padding-bottom: 40px;
    margin: 0 auto;
    text-align: center;
    position: relative;
  }

  .result-btn {
    padding: 12px;
    background-color: #454545;
    color: #fff;
    width: 100px;
    border-radius: 20px;
    margin: 0 auto;
    cursor: pointer;
    position: absolute;
    top: -50px;
    left: 50%;
    transform: translate(-50%, 0);
  }

  .search-input .intro {
    font-size: 28px;
    margin-top: 12px;
    margin-bottom: 24px;
    color: #000;
    font-weight: 700;
  }

  .input-content {
    display: flex;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
    border-radius: 20px;
    gap: 8px;
    justify-content: space-between;
    align-items: center;
    padding: 12px 14px 12px 2px;
    background: #fff;
  }

  .input-style {
    /* width: 80%; */
  }

  .input-style >>> input {
    border: none;
    outline: none;
    padding: 14px;
    margin: 0;
    background-image: none;
    background-color: transparent;
    width: 100%;
    resize: none;
  }

  .input-style >>> input:focus {
    outline: none;
  }

  .select-style {
    width: 100px;
  }

  .select-style >>> input {
    border-radius: 10px;
  }

  .btn-group {
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }

  .btn-group .icon {
    font-size: 20px;
    font-weight: 700;
    cursor: pointer;
    margin-right: 10px;
  }

  .send {
    width: 32px;
    height: 32px;
    line-height: 32px;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.15);
    color: #fff;
    margin-left: 12px;
  }

  .send:hover {
    background-color: rgba(0, 0, 0, 0.4);
  }

  .relative-list {
    margin-bottom: 10px;
    display: flex;
    flex-wrap: wrap;
  }

  .relative-list .list-item {
    margin-right: 4px;
    font-size: 18px;
  }

  .relative-list .icon {
    font-size: 14px;
    font-weight: 700;
    margin-right: 4px;
  }

  .relative-list .btn-title {
    font-size: 14px;
  }

  .smart-model {
    margin-bottom: 20px;
  }
  .smart-model .card-item {
    width: 100%;
    padding: 12px;
    cursor: pointer;
    border-radius: 16px;
    display: flex;
    align-items: center;
  }

  .smart-model .card-icon {
    width: 120px;
    height: 120px;
    margin-right: 12px;
  }

  .smart-model .card-icon img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
  .smart-model .card-title {
    font-size: 24px;
    margin-bottom: 4px;
    font-weight: 700;
    color: #000;
  }
  .smart-model .card-desc {
    overflow: hidden;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    align-items: flex-end;
    display: flex;
    margin-top: 8px;
    margin-bottom: 8px;
    font-size: 13px;
    text-overflow: ellipsis;
    color: rgba(0, 0, 0, 0.5);
  }

  .smart-model .card-status {
    display: flex;
    align-items: center;
    /* height: 16px; */
    font-size: 14px;
    margin-top: 14px;
  }

  .smart-model .card-status div {
    color: rgba(0, 0, 0, 0.5);
    font-size: 12px;
    line-height: 12px;
    margin-top: 2px;
  }

  .smart-model .card-status img {
    width: 16px;
    height: 18px;
    margin-right: 2px;
  }

  .deep {
    display: flex;
    align-items: center;
    margin-right: 10px;
    /* width: 100px; */ /* 注释掉固定宽度,改为自适应 */
    padding: 5px 10px; /* 左右padding一致 */
    border: 1px solid rgba(0, 0, 0, 0.5);
    border-radius: 14px;
    cursor: pointer;
    font-size: 14px;
    white-space: nowrap; /* 防止文字换行 */
  }

  .deep:hover {
    background: #dbeafe;
    border-color: rgba(0, 122, 255, 0.15);
  }
</style>
