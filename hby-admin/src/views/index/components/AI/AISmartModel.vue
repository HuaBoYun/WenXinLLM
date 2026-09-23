<template>
  <div class="container">
    <div v-if="!dialogType" style="height: 100%; width: 810px; margin: 0 auto">
      <div class="search-content">
        <div class="intro">发现AI智能体</div>
        <div
          class="create-model-btn"
          style="display: flex; align-items: center"
        >
          <div class="search">
            <div class="search-input-content">
              <el-input
                class="search-style"
                v-model="chatContent"
                clearable
                placeholder="搜索智能体"
                @keydown.enter.native="onSearch"
              />
            </div>
          </div>
          <el-button
            type="primary"
            round
            icon="el-icon-plus"
            @click="dialogType = 'create-model'"
          >
            创建 AI 智能体
          </el-button>
        </div>
      </div>
      <div class="type-menu">
        <div class="relative-list">
          <div
            class="list-item"
            v-for="item in smartModelCategory"
            :key="item.label"
            :class="{
              'list-item-actived no-hover': item.label === relativeKey,
            }"
            @click="onTypeClick(item)"
          >
            {{ item.label }}
          </div>
        </div>
      </div>
      <div class="relative-cards" v-loading="loading">
        <div
          class="card-item"
          v-for="item in allFillterList"
          :key="item.label"
          @click="onCardClick(item)"
        >
          <div class="card-icon">
            <img
              :src="iconList[Number(item.agentPicture)] || defaultAvatar"
              alt=""
            />
          </div>
          <div class="right">
            <div class="card-title">{{ item.agentName }}</div>
            <div class="card-desc">{{ item.describe }}</div>
            <div class="card-status">
              <div style="display: flex; align-items: center">
                <img
                  src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTcuNjYwNCAxNC4wMDAyQzYuNzgzODQgMTQuMDAwMiA2LjAxODI0IDEzLjg0MyA1LjM2MzYgMTMuNTI4NkM0LjcxMjY1IDEzLjIxNDIgNC4yMDU5NSAxMi43NzQxIDMuODQzNDkgMTIuMjA4MkMzLjQ4MTAzIDExLjY0MjMgMy4yOTk4IDEwLjk4MDMgMy4yOTk4IDEwLjIyMjFDMy4yOTk4IDkuODIyNjIgMy4zMzEyNCA5LjQ2NzU2IDMuMzk0MTIgOS4xNTY4OEMzLjQ2MDY5IDguODQ2MiAzLjU0MDIxIDguNTY2OTYgMy42MzI2NyA4LjMxOTE1QzMuNzI4ODQgOC4wNjc2NSAzLjgyNSA3LjgzMDk0IDMuOTIxMTYgNy42MDkwM0M0LjAxNzMyIDcuMzg3MTIgNC4wOTY4NCA3LjE2NzA1IDQuMTU5NzIgNi45NDg4M0M0LjIyMjU5IDYuNzMwNjIgNC4yNTQwMyA2LjQ5NzYxIDQuMjU0MDMgNi4yNDk4MUM0LjI1NDAzIDYuMTI3NzUgNC4yNDY2NCA1Ljk5NDYgNC4yMzE4NCA1Ljg1MDM2QzQuMjIwNzUgNS43MDI0MiA0LjIxNTIgNS41OTMzMSA0LjIxNTIgNS41MjMwNEM0LjIxNTIgNS40MDA5OCA0LjI1MDMzIDUuMzAxMTIgNC4zMjA2MSA1LjIyMzQ1QzQuMzkwODggNS4xNDU3OCA0LjQ4MzM0IDUuMTA2OTUgNC41OTggNS4xMDY5NUM0Ljc5NDAyIDUuMTA2OTUgNC45OTE4OSA1LjE2NjEzIDUuMTkxNjIgNS4yODQ0OEM1LjM5NTA0IDUuNDAyODMgNS41NzYyNyA1LjU2NzQyIDUuNzM1MyA1Ljc3ODI0QzUuODk4MDQgNS45ODUzNiA2LjAyMTk0IDYuMjI1NzcgNi4xMDcwMSA2LjQ5OTQ2TDUuODI5NjIgNi41NTQ5NEM1LjkxODM4IDYuMzk5NiA1Ljk3Mzg2IDYuMjYyNzUgNS45OTYwNSA2LjE0NDRDNi4wMjE5NCA2LjAyMjM0IDYuMDM0ODkgNS44OTg0NCA2LjAzNDg5IDUuNzcyNjlDNi4wMzExOSA1LjQwNjUzIDUuOTYyNzYgNS4wNTg4NyA1LjgyOTYyIDQuNzI5NjlDNS43MDAxNyA0LjQwMDUyIDUuNTMzNzMgNC4wODc5OSA1LjMzMDMxIDMuNzkyMTFDNS4xMzA1OSAzLjQ5NjIyIDQuOTIxNjIgMy4yMTY5OCA0LjcwMzQxIDIuOTU0MzhDNC42NDQyMyAyLjg4NzgxIDQuNTk5ODUgMi44MTkzOSA0LjU3MDI2IDIuNzQ5MTFDNC41NDA2NyAyLjY3ODg0IDQuNTI1ODggMi42MDg1NyA0LjUyNTg4IDIuNTM4MjlDNC41MjU4OCAyLjM2MDc2IDQuNjAxNyAyLjIyNzYxIDQuNzUzMzQgMi4xMzg4NUM0LjkwODY4IDIuMDQ2MzggNS4xMTc2NCAyLjAwMDE1IDUuMzgwMjQgMi4wMDAxNUM1Ljg2ODQ1IDIuMDAwMTUgNi4zODI1NSAyLjA1OTMzIDYuOTIyNTQgMi4xNzc2OEM3LjQ2NjIzIDIuMjkyMzQgOC4wMDQzNyAyLjQ3MzU3IDguNTM2OTYgMi43MjEzN0M5LjA3MzI1IDIuOTY1NDggOS41ODE4IDMuMjc4MDEgMTAuMDYyNiAzLjY1ODk2QzEwLjU0MzQgNC4wMzYyMSAxMC45Njg4IDQuNDg1NTkgMTEuMzM4NiA1LjAwNzA5QzExLjcxMjIgNS41MjQ4OSAxMi4wMDYyIDYuMTE4NTEgMTIuMjIwNyA2Ljc4Nzk1QzEyLjQzNTIgNy40NTczOSAxMi41NDI1IDguMjA2MzUgMTIuNTQyNSA5LjAzNDgzQzEyLjU0MjUgOS43NzgyNCAxMi40MjYgMTAuNDU1MSAxMi4xOTMgMTEuMDY1M0MxMS45NjM3IDExLjY3NTYgMTEuNjMyNyAxMi4xOTkgMTEuMTk5OSAxMi42MzU0QzEwLjc2NzIgMTMuMDcxOCAxMC4yNTEyIDEzLjQwODQgOS42NTIwNyAxMy42NDUxQzkuMDU2NjEgMTMuODgxOCA4LjM5MjcyIDE0LjAwMDIgNy42NjA0IDE0LjAwMDJaTTcuNzg4IDEyLjQ0MTJDOC4yMjgxMyAxMi40NDEyIDguNTkyNDQgMTIuMzQ1IDguODgwOTMgMTIuMTUyN0M5LjE3MzExIDExLjk2MDQgOS4zOTEzMyAxMS43MDcgOS41MzU1NyAxMS4zOTI3QzkuNjc5ODEgMTEuMDc4MyA5Ljc1MTk0IDEwLjczOCA5Ljc1MTk0IDEwLjM3MTlDOS43NTE5NCAxMC4wMDk0IDkuNjgxNjYgOS42NDUwOSA5LjU0MTEyIDkuMjc4OTNDOS40MDQyNyA4LjkxMjc3IDkuMTk5IDguNTc0MzYgOC45MjUzMSA4LjI2MzY4QzguNjUxNjIgNy45NTMgOC4zMTEzNSA3LjcwMTQ5IDcuOTA0NTEgNy41MDkxN0M3Ljg3ODYyIDcuNDk4MDcgNy44NTgyNyA3LjQ5OTkyIDcuODQzNDggNy41MTQ3MkM3LjgyODY5IDcuNTI1ODEgNy44MjMxNCA3LjU0NDMgNy44MjY4NCA3LjU3MDE5QzcuODc0OTIgOC4wMjUxMiA3Ljg2NzUyIDguNDQ4NiA3LjgwNDY1IDguODQwNjVDNy43NDE3NyA5LjIyOSA3LjYzNjM2IDkuNTIzMDQgNy40ODg0MiA5LjcyMjc2QzcuNDE4MTUgOS41NjM3MiA3LjMzODYzIDkuNDE1NzggNy4yNDk4NiA5LjI3ODkzQzcuMTY0OCA5LjEzODM5IDcuMDU3NTQgOS4wMTA3OSA2LjkyODA5IDguODk2MTNDNi45MDk2IDguODgxMzQgNi44OTExIDguODc3NjQgNi44NzI2MSA4Ljg4NTAzQzYuODU3ODIgOC44ODg3MyA2Ljg0ODU3IDguOTAzNTMgNi44NDQ4NyA4LjkyOTQyQzYuODIyNjggOS4wODg0NiA2Ljc3MDkgOS4yMzQ1NSA2LjY4OTUzIDkuMzY3N0M2LjYwODE2IDkuNDk3MTUgNi41MTk0IDkuNjMzOTkgNi40MjMyNCA5Ljc3ODI0QzYuMzI3MDcgOS45MTg3OCA2LjI0MjAxIDEwLjA3NzggNi4xNjgwMyAxMC4yNTU0QzYuMDk3NzYgMTAuNDI5MiA2LjA2MjYzIDEwLjYzODIgNi4wNjI2MyAxMC44ODIzQzYuMDYyNjMgMTEuMzQ4MyA2LjIxOTgxIDExLjcyNTUgNi41MzQxOSAxMi4wMTRDNi44NTIyNyAxMi4yOTg4IDcuMjcwMiAxMi40NDEyIDcuNzg4IDEyLjQ0MTJaIiBmaWxsPSIjOTk5OTk5Ii8+Cjwvc3ZnPgo="
                  alt=""
                />
                <div>{{ item.agentType }}</div>
              </div>
              <div style="display: flex; align-items: center">
                <el-button type="text" @click="onModify(item)">修改</el-button>
                <el-button type="text" @click="onDelete(item)">删除</el-button>
                <el-button
                  type="text"
                  @click="onSend(item)"
                  :disabled="!!item.flagIssued"
                >
                  {{ item.flagIssued ? '已下发' : '下发' }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="search-input">
        <div class="input-content">
          <el-input
            class="input-style"
            v-model="searchKey"
            clearable
            type="textarea"
            :rows="1"
            autosize
            placeholder="输入你要撰写的主题"
            @keydown.enter.native="onAgent"
          />
          <div class="btn-group">
            <div class="switch-group">
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
                <i v-if="!think" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
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
                搜索资料
                <i v-if="!search" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
              <div
                @click="editor = !editor"
                class="deep"
                :style="`${
                  editor
                    ? 'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);'
                    : ''
                }`"
              >
                文档编辑器
                <i v-if="!editor" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
            </div>

            <div class="icons">
              <voice class="icon" @getMessage="getMessage" />
              <i class="icon el-icon-top send" @click="onAgent"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="dialogType === 'create-model'">
      <div class="create-model">
        <i class="el-icon el-icon-arrow-left my-icon" @click="back"></i>
        <div class="title">智能体</div>
        <div class="form-content">
          <el-form
            ref="form"
            :model="form"
            label-width="80px"
            label-position="top"
            style="width: 568px"
          >
            <div class="upload-avator">
              <img
                :src="
                  form.agentPicture
                    ? iconList[form.agentPicture]
                    : defaultAvatar
                "
                alt=""
              />
              <i
                class="el-icon el-icon-plus"
                style="cursor: pointer"
                @click="selectIconVisible = true"
              ></i>
            </div>
            <el-form-item label="编号">
              <el-input
                v-model="form.agentNo"
                placeholder="输入编号"
              ></el-input>
            </el-form-item>
            <el-form-item label="名称">
              <el-input
                v-model="form.agentName"
                placeholder="输入名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="分类">
              <el-select
                v-model="form.agentType"
                placeholder="选择分类"
                style="width: 100%"
              >
                <el-option
                  v-for="item in smartModelCategory"
                  :value="item.label"
                  :label="item.label"
                  :key="item.label"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设定描述">
              <el-input
                v-model="form.describe"
                type="textarea"
                rows="5"
                placeholder="示例：你是一位经验丰富的英语老师，拥有激发学生学习热情的教学方法。你善于运用幽默和实际应用案例，使对话充满趣味。"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                round
                @click="(e) => onCreate(null)"
                class="save-btn"
              >
                创建 AI 智能体
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <div v-if="dialogType === 'agent-chat'" class="agent-chat">
      <div
        style="margin: 0 auto; position: relative"
        :style="`width: 52%;`"
        id="iframeRef"
      >
        <i
          class="el-cion el-icon-back"
          style="
            cursor: pointer;
            font-size: 25px;
            position: absolute;
            left: 15px;
            top: 15px;
          "
          @click="back"
        ></i>
        <iframe
          :src="iframeUrl"
          style="width: 100%; height: 100%; min-height: 700px"
          frameborder="0"
          allow="microphone"
        ></iframe>
      </div>
      <div class="right-content" v-if="review">
        <div class="top-nav">
          <i class="icon el-icon-close" @click="review = false"></i>
          <div class="title">beijing_school_district_report.html</div>
          <el-button type="info" size="mini">预览</el-button>
          <el-button class="btn" type="default" size="mini">代码</el-button>
          <img class="icon" :src="fullscreenSvg" alt="" />
          <i class="icon el-icon-download"></i>
        </div>
        <div class="file-content">
          Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dolorem quo
          maiores aliquam incidunt temporibus, non nostrum deserunt expedita.
          Error, nihil eum exercitationem libero eveniet in provident officiis
          repellendus eaque fugiat! Pariatur repellat facilis fugiat, incidunt
          dolore illum nisi. Deleniti eveniet ex corrupti qui fugiat, libero ea
          repellat, culpa expedita laborum sint et facere laboriosam vel iste
          non sit dolorem cum! Delectus, quia! Eaque consectetur vitae,
          reiciendis neque officia a magnam qui vero eius ut fugit voluptatem
          cum veniam nobis natus necessitatibus adipisci deserunt, blanditiis
          esse? Consequatur voluptatem tenetur nisi commodi. Commodi ipsum a
          quos! Impedit non at sint iusto voluptas labore, facere pariatur qui
          quas dolor magni inventore facilis aut ex aspernatur eligendi omnis
          doloribus dolores numquam quidem excepturi ut. Neque dignissimos
          minus, itaque deleniti voluptatibus voluptate maxime repellendus
          tempore a non, aperiam, eveniet omnis qui numquam optio dolores
          architecto? Est aspernatur delectus ullam excepturi ea repellendus aut
          eligendi neque? Pariatur dignissimos quisquam minima molestias commodi
          quos vitae facilis libero, repellat ut corrupti quae debitis odio
          architecto? Minus labore ipsam aliquam deserunt suscipit obcaecati?
          Excepturi optio delectus consequatur voluptatibus recusandae? Incidunt
          quo pariatur reprehenderit sit illum, labore optio enim minima
          architecto, quis reiciendis eum? Autem velit molestiae ullam, nam
          perspiciatis ex adipisci, at hic, modi magni quod neque nemo
          reiciendis. Voluptates dolor, doloribus perferendis magni iste
          aspernatur. Tempora ducimus esse fuga ipsam recusandae quos? Id
          nesciunt, minima eveniet deleniti inventore ut possimus repellat at
          eaque culpa officia consectetur temporibus consequuntur? Suscipit sunt
          quidem fuga est veritatis officiis delectus nemo non cum minus quis
          esse quia accusantium dolorum quos ipsam vero nam corporis quibusdam,
          vitae facere maxime. Alias mollitia eligendi illo. Dicta minima modi
          excepturi asperiores quaerat voluptatibus, iusto odio animi eligendi,
          iste quis quidem repudiandae eaque ab, architecto voluptatem?
          Provident nulla soluta quasi officia est distinctio tenetur ab
          cupiditate commodi. Lorem ipsum dolor sit amet consectetur adipisicing
          elit. Facilis recusandae cupiditate nihil accusantium vitae voluptatem
          perspiciatis, eveniet vel architecto libero nesciunt modi cum,
          deleniti ad ducimus sed laborum amet. Quo? Lorem, ipsum dolor sit amet
          consectetur adipisicing elit. Modi provident unde corporis inventore
          pariatur, distinctio ut vel rerum, necessitatibus maiores nam ad
          ducimus error amet eveniet nesciunt vitae repellendus est. Molestias
          corporis cumque nesciunt ab at ad facere officiis quaerat suscipit
          aut, culpa repellendus quas nihil atque saepe velit asperiores aliquid
          praesentium nobis corrupti rerum in perspiciatis consectetur.
          Necessitatibus, incidunt! Nobis voluptates maiores dolorum dolor nihil
          quae consectetur? Voluptas illum ex optio expedita beatae? Suscipit
          sed maiores eos officia optio. Beatae ipsam animi aspernatur illum ad
          in laudantium aliquam doloribus. Debitis, explicabo magnam officiis
          quasi esse harum architecto sunt suscipit qui officia cum voluptate
          necessitatibus cumque nihil illum aspernatur perferendis quae ipsam
          quaerat cupiditate saepe! Laboriosam nisi accusantium dolores laborum.
          Cum nam deleniti assumenda. Vel expedita esse corporis cum
          dignissimos, quia veritatis? Nisi perferendis officia ullam, quo
          dolore quaerat alias praesentium temporibus omnis aliquid a voluptatum
          repellat, illo enim consequatur? Saepe, nulla dolore delectus
          eligendi, numquam ut quaerat aliquid nesciunt, consequatur accusamus
          voluptatum. Quo rem aliquam vitae dolorum voluptate autem, velit
          repellendus sapiente repudiandae quas quibusdam et ullam, dicta
          labore. Minus nam consequatur nihil ducimus facilis incidunt
          asperiores, ut rem. Blanditiis adipisci neque aliquam quos quidem
          officia hic nam reiciendis labore voluptatum. Reiciendis neque
          quibusdam harum recusandae commodi quasi facilis. Natus ipsa facilis,
          molestiae soluta dolores, ex cupiditate ullam doloribus sapiente,
          officiis aliquid quisquam recusandae quis libero. Placeat velit odit
          voluptatibus eum blanditiis similique ex aliquid quisquam! Voluptas,
          doloremque ut. Praesentium beatae quas ipsa rem ullam possimus quasi
          officia ratione quis quidem voluptate tempore sunt provident adipisci,
          sit hic inventore id perferendis repellat ab? Dolorem ipsum cum
          laborum deserunt recusandae. Atque magnam a nisi omnis corrupti.
          Expedita cum odit accusamus maiores, unde quam in quasi possimus
          impedit illum repudiandae at voluptatum soluta ratione quas sapiente!
          Accusantium labore minus omnis quo. Fuga dolores minus consequatur
          voluptatibus deleniti. Unde numquam nemo eveniet sunt rerum fugit,
          similique iure architecto odio deleniti quam et inventore vitae,
          tenetur, corporis fugiat aspernatur distinctio quisquam ad.
          Voluptatibus! Ducimus libero labore voluptate? Temporibus dicta
          officiis magnam dolores a veniam obcaecati expedita quidem ducimus ad,
          numquam nobis cum illum quaerat debitis! Qui dicta quibusdam, nobis
          atque amet dolorem facere. Vero quam explicabo dolor eos delectus.
          Perspiciatis, nobis culpa numquam minima voluptatum temporibus
          cupiditate iure, dignissimos dolor doloremque ipsa tempore nam
          voluptas ratione cum, asperiores eos vero ea dolorem molestiae? Beatae
          sequi eius nemo accusantium similique suscipit commodi, id sapiente
          ab. Voluptas assumenda nihil, ipsa vero animi deleniti quam, ducimus
          consequuntur rerum perspiciatis magni, dolores tenetur numquam impedit
          veniam mollitia? Laborum veritatis dolor odit, dolores beatae quidem
          provident fugiat explicabo repellendus at voluptas facilis ipsa
          eligendi velit quam doloribus aliquam qui nobis molestiae, voluptatem
          architecto inventore illum ad. Optio, autem! Illum quisquam tempore
          eligendi blanditiis id dolor totam? Amet voluptate assumenda vero
          beatae officia tempora voluptas? Ut aperiam, ducimus modi tempore
          neque optio officia, rem eaque porro quod vitae similique? Quos
          dignissimos, quaerat velit, sint mollitia quod maxime dolore commodi
          veniam voluptates distinctio excepturi magnam et? Quos corrupti dicta,
          nisi totam obcaecati, quae aperiam quaerat quam necessitatibus
          pariatur dignissimos corporis! Iusto accusantium pariatur dolores
          animi delectus qui nulla corporis repudiandae. Asperiores eum est
          saepe illum delectus possimus omnis, obcaecati quia distinctio,
          facilis nostrum similique doloribus. Cumque, eos. Delectus,
          perferendis odit. Molestias ea, possimus veniam eius iste vitae,
          placeat officia ex voluptatem minus cum nisi quo laudantium nobis.
          Molestias, delectus quidem expedita praesentium accusantium, quod
          dolor dolores, nemo temporibus laboriosam atque. Porro dolorem eveniet
          perferendis, voluptates incidunt nostrum id nihil sunt perspiciatis
          explicabo, enim libero beatae consectetur voluptate ut eum ullam
          facere eaque, quo cumque illo quam? Ut molestiae ad reprehenderit?
        </div>
        <div class="footer">
          <i class="el-icon-back"></i>
          <div class="page">1</div>
          <i class="el-icon-right"></i>
        </div>
      </div>
    </div>

    <!-- 选择头像 -->
    <el-dialog
      title="选择图标"
      :visible.sync="selectIconVisible"
      append-to-body
      width="500px"
      @close="selectIconVisible = false"
    >
      <div
        v-if="selectIconVisible"
        class="icon-list"
        style="
          display: grid;
          grid-template-columns: repeat(7, 1fr);
          gap: 12px;
          height: 300px;
          overflow: auto;
        "
      >
        <div
          class="icon-item"
          v-for="(url, index) in iconList"
          style="width: 50px; height: 50px"
        >
          <img
            :src="iconList[index]"
            alt=""
            @click="onSelectIcon(url, index)"
            style="width: 100%; cursor: pointer"
          />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import mapData from '@/views/index/components/AI/mock/dataMap.js'
  import {
    getAgentList,
    sageOrUpdateAgent,
    deleteAgent,
  } from '@/api/ai/index.js'
  import { getToken } from '@/utils/token'
  import fullscreenSvg from '@/views/index/components/AI/icon/fullscreen.svg'
  import voice from '@/views/index/components/AI/voice.vue'
  import axios from 'axios'
  import request from '@/utils/request'

  // 创建一个可以在服务端运行（读取 Set-Cookie）的 axios 实例
  // 注意：这个配置只在服务端渲染或特殊环境下有效
  // 在浏览器中仍然无法读取 Set-Cookie
  const axiosInstance = axios.create({
    // 关键配置：允许读取 Set-Cookie 响应头
    // 注意：在浏览器环境中这个配置不会生效，因为浏览器的安全限制
    baseURL: 'https://www.huabao.example.com',
    withCredentials: true,
  })

  // 如果需要在浏览器中获取 cookie，需要：
  // 1. wenxinAgent 后端在响应体中返回 token
  // 2. 或者使用后端代理，通过后端转发请求
  // 3. 或者使用同源 iframe + postMessage 通信

  import defaultAvatar from './../AI/icon/default-avatar.png'
  import a0 from './../AI/icon/a0.png'
  import a1 from './../AI/icon/a1.png'
  import a2 from './../AI/icon/a2.png'
  import a3 from './../AI/icon/a3.png'
  import a4 from './../AI/icon/a4.png'
  import a5 from './../AI/icon/a5.png'
  import a6 from './../AI/icon/a6.png'
  import a7 from './../AI/icon/a7.png'
  import a8 from './../AI/icon/a8.png'
  import a9 from './../AI/icon/a9.png'
  import a10 from './../AI/icon/a10.png'
  import a11 from './../AI/icon/a11.png'
  import a12 from './../AI/icon/a12.png'
  import a13 from './../AI/icon/a13.png'
  import a14 from './../AI/icon/a14.png'
  import a15 from './../AI/icon/a15.png'
  import a16 from './../AI/icon/a16.png'
  import a17 from './../AI/icon/a17.png'
  import a18 from './../AI/icon/a18.png'
  import a19 from './../AI/icon/a19.png'
  import a20 from './../AI/icon/a20.png'
  import a21 from './../AI/icon/a21.png'
  import a22 from './../AI/icon/a22.png'
  import a23 from './../AI/icon/a23.png'
  import a24 from './../AI/icon/a24.png'
  import a25 from './../AI/icon/a25.png'
  import a26 from './../AI/icon/a26.png'
  import a27 from './../AI/icon/a27.png'
  import a28 from './../AI/icon/a28.png'
  import a29 from './../AI/icon/a29.png'
  import a30 from './../AI/icon/a30.png'
  import a31 from './../AI/icon/a31.png'
  import a32 from './../AI/icon/a32.png'
  import a33 from './../AI/icon/a33.png'
  import a34 from './../AI/icon/a34.png'
  import a35 from './../AI/icon/a35.png'
  import a36 from './../AI/icon/a36.png'
  import a37 from './../AI/icon/a37.png'
  import a38 from './../AI/icon/a38.png'

  export default {
    components: { voice },
    data() {
      return {
        defaultAvatar,
        loading: false,
        searchKey: '',
        chatContent: '',
        source: '参考资料',
        userName: '',
        relativeKey: '全部',
        value1: '',
        value2: '',
        selectIconVisible: false,
        form: {
          id: '',
          agentNo: '',
          agentType: '',
          agentName: '',
          describe: '',
          agentPicture: '',
        },
        iconList: [
          a0,
          a1,
          a2,
          a3,
          a4,
          a5,
          a6,
          a7,
          a8,
          a9,
          a10,
          a11,
          a12,
          a13,
          a14,
          a15,
          a16,
          a17,
          a18,
          a19,
          a20,
          a21,
          a22,
          a23,
          a24,
          a25,
          a26,
          a27,
          a28,
          a29,
          a30,
          a31,
          a32,
          a33,
          a34,
          a35,
          a36,
          a37,
          a38,
        ],
        smartModelCategory: mapData.smartModelCategory,
        allList: [],
        dialogType: '',
        curCard: {},
        review: true,
        iframeUrl: '',
        fullscreenSvg,
        think: false,
        search: false,
        editor: false,
      }
    },
    computed: {
      allFillterList() {
        return this.allList.filter(
          (x) => this.relativeKey === '全部' || x.agentType === this.relativeKey
        )
      },
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      this.fetchData()
      document.addEventListener('visibilitychange', this.changeTab)
    },
    beforeDestroy() {
      document.removeEventListener('visibilitychange', this.changeTab)
    },
    methods: {
      // 从 document.cookie 中读取指定名称的 cookie 值
      getCookie(name) {
        const match = document.cookie.match(
          new RegExp('(?:^|; )' + name + '=([^;]*)')
        )
        return match ? decodeURIComponent(match[1]) : null
      },
      // 统一的 wenxinAgent 登录方法，登录成功后从响应头读取 x-csrf-token
      async wenxinAgentLogin() {
        // 使用原生 axios 实例直接请求，以便获取完整的响应头
        const response = await axiosInstance({
          url: `/console/wenxinAgent/login`,
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: {
            email: 'me@admin.com',
            password: 'WVdSdGFXNHhNak0wTlRZPQ==',
            language: 'zh-Hans',
            remember_me: true,
          },
          withCredentials: true,
        })

        // 从响应头中获取 x-csrf-token（已在 access-control-expose-headers 中暴露）
        const csrfToken = response?.headers?.['x-csrf-token']
        console.log('wenxinAgentLogin 完成，从响应头获取的 x-csrf-token:', csrfToken)
        return { csrfToken }
      },
      changeTab() {
        if (document.hidden) {
          // 页面关闭
          console.log('页面关闭')
        } else {
          // 页面打开
          console.log('页面打开')
          this.fetchData()
        }
      },
      async fetchData() {
        this.loading = true
        getAgentList({ pageSize: 99999 })
          .then((res) => {
            if (res && res.data) {
              this.allList = res.data.tlist || []
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      // 调用 wenxinAgent 获取应用列表
      async fetchWenxinAgentApps() {
        this.loading = true
        try {
          const { csrfToken } = await this.wenxinAgentLogin()
          // access_token 在 HttpOnly cookie 中，浏览器请求时自动携带
          // 只需在请求头中附加 csrf_token 完成鉴权
          const appsRes = await request({
            url: `/console/wenxinAgent/apps`,
            method: 'get',
            headers: {
              'Content-Type': 'application/json',
              'X-Csrf-Token': csrfToken || undefined,
            },
            withCredentials: true,
          })
          console.log('wenxinAgent应用列表响应:', appsRes)
          return appsRes
        } catch (error) {
          console.error('获取wenxinAgent应用列表失败:', error)
          throw error
        } finally {
          this.loading = false
        }
      },
      onModify(item) {
        Object.assign(this.form, item)
        this.onCreate(item.agentId)
      },
      onSend(item) {
        this.$confirm('确定下发该智能体?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success',
        })
          .then(() => {
            if (item.id) {
              sageOrUpdateAgent({ id: item.id, flagIssued: 1 }).then((res) => {
                if (res.code) {
                  this.$message({
                    type: 'success',
                    message: '下发成功!',
                  })
                  this.fetchData()
                }
              })
            } else {
              this.$message({
                type: 'error',
                message: '下发失败!',
              })
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消下发',
            })
          })
      },
      onDelete(item) {
        this.$confirm('确定删除该智能体?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            // 调用登录接口获取 cookie
            axios({
              url: `/wenxinAgent-api/login`,
              method: 'post',
              headers: {
                'Content-Type': 'application/json',
                'Cookie': 'locale=zh-Hans',
              },
              data: {
                email: 'me@admin.com',
                password: 'WVdSdGFXNHhNak0wTlRZPQ==',
                language: 'zh-Hans',
                remember_me: true,
              },
              withCredentials: true,
            }).then((loginRes) => {
              console.log('loginRes', loginRes)
              // 新接口返回格式：{"result":"success"}，cookie 会自动设置到浏览器
              if (
                loginRes &&
                loginRes.data &&
                loginRes.data.result === 'success'
              ) {
                // 删除本地记录
                deleteAgent({ id: item.id })
                  .then((deleteRes) => {
                    if (deleteRes && deleteRes.code == 200) {
                      this.$message({
                        type: 'success',
                        message: '删除成功！',
                      })
                      this.fetchData()
                    } else {
                      this.$message({
                        type: 'error',
                        message: '删除失败！',
                      })
                    }
                  })
                  .catch((err) => {
                    console.error('删除本地记录失败:', err)
                    this.$message({
                      type: 'error',
                      message: '删除失败！',
                    })
                  })
              }
            })
            .catch((err) => {
              console.error('登录失败:', err)
              this.$message({
                type: 'error',
                message: '登录失败！',
              })
            })
          })
          .catch(() => {
            this.$message({ type: 'info', message: '已取消删除' })
          })
      },
      onSelectIcon(url, index) {
        this.form.agentPicture = index
        this.selectIconVisible = false
      },
      onSearch() {},
      onTypeClick(item) {
        this.relativeKey = item.label
      },
      onCardClick(item) {
        console.log('item', item)
        this.curCard = item
        this.searchKey = item.describe
        this.iframeUrl = item.jumpAddress
      },
      async onCreate(agentId) {
        if (!this.form.agentPicture)
          return this.$message.error('请选择智能体图标！')
        if (!this.form.agentNo) return this.$message.error('请输入智能体编号！')
        if (!this.form.agentName)
          return this.$message.error('请输入智能体名称！')
        if (!this.form.agentType)
          return this.$message.error('请选择智能体分类！')

        try {
          const { csrfToken } = await this.wenxinAgentLogin()

          if (agentId) {
            const skipUrl = `https://www.huabao.example.com/app/${agentId}/configuration`
            console.log('skipUrl', skipUrl)
            window.open(skipUrl)
            return
          }

          // 通过后端代理调用创建应用接口
          const res = await request({
            url: `/console/wenxinAgent/apps`,
            method: 'post',
            headers: {
              'Content-Type': 'application/json',
              'X-Csrf-Token': csrfToken || undefined,
            },
            withCredentials: true,
            data: {
              name: this.form.agentName,
              icon_type: 'emoji',
              icon: '🤖',
              icon_background: '#FFEAD5',
              mode: 'chat',
              description: this.form.describe,
            },
          })
          console.log('创建应用成功:', res)
          const appId = res?.id || res?.data?.id
          if (appId) {
            // 创建后拉取应用详情，从 site.access_token 拼接 jumpAddress
            let jumpAddress = ''
            try {
              const detailRes = await request({
                url: `/console/wenxinAgent/apps/${appId}`,
                method: 'get',
                headers: { 'X-Csrf-Token': csrfToken || undefined },
                withCredentials: true,
              })
              const accessToken =
                detailRes?.site?.access_token ||
                detailRes?.data?.site?.access_token
              if (accessToken) {
                jumpAddress = `https://www.huabao.example.com/chatbot/${accessToken}`
              }
            } catch (e) {
              console.warn('获取应用详情失败，jumpAddress 将为空', e)
            }
            const skipUrl = `https://www.huabao.example.com/app/${appId}/configuration`
            sageOrUpdateAgent({
              agentNo: this.form.agentNo,
              agentName: this.form.agentName,
              agentType: this.form.agentType,
              describe: this.form.describe,
              agentPicture: this.form.agentPicture,
              agentId: appId,
              jumpAddress,
            })
              .then(() => this.getList())
              .catch(() => {})
            this.back()
            window.open(skipUrl)
          }
        } catch (err) {
          console.error('操作失败:', err)
          this.$message.error(
            `操作失败: ${
              err.response?.data?.message || err.message || '未知错误'
            }`
          )
        }
      },
      back() {
        this.dialogType = ''
        this.form.id = ''
        this.form.agentNo = ''
        this.form.agentType = ''
        this.form.agentName = ''
        this.form.describe = ''
        this.form.agentPicture = ''
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
      onAgent() {
        if (!this.curCard || !this.curCard.id)
          return this.$message.error('请选择智能体！')
        if (!this.iframeUrl) return this.$message.error('智能体对象创建失败！')
        this.dialogType = 'agent-chat'
      },
    },
  }
</script>

<style scoped>
  .container {
    /* padding-bottom: 150px; */
    /* height: auto; */
    /* width: 810px; */
    overflow: hidden;
  }

  .agent-chat {
    display: flex;
    height: 100%;
    overflow: hidden;
    /* padding: 50px 0; */
  }

  .text-content {
    margin-top: 50px;
  }

  .search-input {
    width: 810px;
    text-align: center;
    position: absolute;
    bottom: 0;
    padding-bottom: 20px;
  }

  .search-content {
    margin-top: 50px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .intro {
    font-size: 24px;
    margin-top: 12px;
    color: #000;
    text-align: left;
  }

  .type-menu {
    display: flex;
    justify-content: space-between;
    margin-top: 30px;
    align-items: center;
  }

  .relative-list {
    display: flex;
    flex-wrap: wrap;
  }

  .relative-list .list-item {
    margin-right: 4px;
    margin-bottom: 6px;
    font-size: 14px;
    border-radius: 8px;
    padding: 7px 16px;
    cursor: pointer;
    border: 1px solid rgba(0, 0, 0, 0.08);
  }

  .relative-list .list-item:hover {
    background: rgba(0, 0, 0, 0.06);
  }

  .relative-list .list-item-actived:hover {
    background: #232629;
  }

  .relative-list .list-item-actived {
    background: #232629;
    color: #fff;
  }

  .relative-list .icon {
    font-size: 14px;
    font-weight: 700;
    margin-right: 4px;
  }
  .relative-list .btn-title {
    font-size: 14px;
  }

  .relative-cards {
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
    margin-bottom: 20px;
    overflow: auto;
    max-height: 92%;
    padding-bottom: 50px;
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
  }

  .card-item .right {
    flex: 1;
  }

  .card-item {
    border: 1px solid rgba(0, 0, 0, 0.08);
    width: 400px;
    padding: 12px;
    cursor: pointer;
    border-radius: 16px;
    display: flex;
    align-items: center;
    height: 100px;
    background: #fff;
  }

  .card-item:hover {
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
  }

  .card-icon {
    width: 48px;
    height: 48px;
    margin-right: 12px;
  }

  .card-icon img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
  .card-title {
    font-size: 14px;
    margin-bottom: 4px;
    font-weight: 700;
  }
  .card-desc {
    overflow: hidden;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    margin-top: 8px;
    margin-bottom: 8px;
    font-size: 13px;
    text-overflow: ellipsis;
    display: -webkit-box;
    color: rgba(0, 0, 0, 0.5);
  }

  .card-status {
    display: flex;
    align-items: center;
    height: 16px;
    justify-content: space-between;
  }

  .card-status div {
    color: rgba(0, 0, 0, 0.5);
    font-size: 12px;
    line-height: 12px;
    margin-top: 2px;
  }

  .card-status img {
    width: 16px;
    height: 16px;
    margin-right: 2px;
  }

  .search {
    text-align: center;
    width: 150px;
    margin-right: 10px;
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

  .search-input-content {
    display: flex;
    border: 1px solid rgba(0, 0, 0, 0.1);
    /* box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1); */
    border-radius: 20px;
    gap: 8px;
    justify-content: space-between;
    align-items: center;
    overflow: hidden;
  }

  .input-content {
    display: flex;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
    border-radius: 20px;
    display: flex;
    gap: 8px;
    justify-content: space-between;
    padding: 12px 14px 12px 2px;
    flex-direction: column;
    background: #fff;
  }

  .input-style >>> textarea {
    border: none;
    outline: none;
    padding: 14px;
    margin: 0;
    background-image: none;
    background-color: transparent;
    width: 100%;
    resize: none;
    font-size: 16px;
  }

  .input-style >>> textarea:focus {
    outline: none;
  }

  .input-style >>> .textarea_ai {
    display: none;
  }

  .btn-group {
    display: flex;
    justify-content: space-between;
    margin-left: 14px;
    /* margin-top: 0px; */
  }

  .switch-group {
    display: flex;
    align-items: center;
  }

  .icons .icon {
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

  .search-style {
    /* width: 80%; */
  }

  .deep {
    display: flex;
    align-items: center;
    margin-right: 10px;
    padding: 5px 8px;
    border: 1px solid rgba(0, 0, 0, 0.5);
    border-radius: 14px;
    cursor: pointer;
    font-size: 14px;
  }

  .deep:hover {
    background: #dbeafe;
    border-color: rgba(0, 122, 255, 0.15);
  }

  .btn-icon {
    width: 18px;
    height: 18px;
    font-size: 18px;
    margin-right: 0;
    margin-left: 4px;
  }

  .search-style >>> input {
    border: none;
    outline: none;
    padding: 14px;
    margin: 0;
    background-image: none;
    background-color: transparent;
    width: 100%;
    resize: none;
    background: #fff;
  }

  .search-style >>> input:focus {
    outline: none;
  }

  .create-model {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .create-model .title {
    font-size: 16px;
    font-weight: 700;
    height: 56px;
    line-height: 56px;
    color: #000;
  }

  .create-model .save-btn {
    width: 100%;
    height: 48px;
    background: #06f;
    color: #fff;
    font-size: 16px;
    font-weight: 600;
    margin-top: 20px;
  }

  .upload-avator {
    width: 100px;
    height: 100px;
    position: relative;
    margin: 50px 0 50px 0;
    margin: 0 auto;
  }

  .upload-avator img {
    width: 100%;
    height: 100%;
  }

  .upload-avator i {
    width: 34px;
    height: 34px;
    position: absolute;
    right: 0;
    bottom: 0;
    background: #06f;
    border-radius: 50%;
    text-align: center;
    line-height: 34px;
    font-size: 18px;
    font-weight: 700;
    color: #fff;
  }

  .form-content >>> input {
    background: rgba(0, 0, 0, 0.04);
    border: none;
    box-sizing: border-box;
    color: inherit;
    outline: none;
    padding: 12px 0;
    padding-left: 12px;
    padding-right: 12px;
    width: 100%;
    border-radius: 8px;
    font-size: 16px;
    height: 42px;
  }
  .form-content >>> textarea {
    background: rgba(0, 0, 0, 0.04);
    border: none;
    box-sizing: border-box;
    color: inherit;
    outline: none;
    padding: 12px 0;
    padding-left: 12px;
    padding-right: 12px;
    width: 100%;
    border-radius: 8px;
    font-size: 16px;
  }

  .form-content >>> input:focus {
    outline: auto;
  }
  .form-content >>> textarea:focus {
    outline: auto;
  }

  .my-icon {
    position: absolute;
    top: 8px;
    left: 24px;
    width: 40px;
    height: 40px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
  }

  .my-icon:hover {
    background: rgba(0, 0, 0, 0.06);
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
</style>
