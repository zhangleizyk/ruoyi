<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            node-key="id"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="用户名称" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入用户名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="用户状态"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:user:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:user:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:user:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['system:user:import']"
            >导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
            >导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <div ref="main" style="width: 100%; height: 1750px;"></div>

<!--        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">-->
<!--          <el-table-column type="selection" width="50" align="center" />-->
<!--          <el-table-column label="用户编号" align="center" key="userId" prop="userId" v-if="columns[0].visible" />-->
<!--          <el-table-column label="用户名称" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible" width="120" />-->
<!--          <el-table-column label="状态" align="center" key="status" v-if="columns[5].visible">-->
<!--            <template slot-scope="scope">-->
<!--              <el-switch-->
<!--                v-model="scope.row.status"-->
<!--                active-value="0"-->
<!--                inactive-value="1"-->
<!--                @change="handleStatusChange(scope.row)"-->
<!--              ></el-switch>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">-->
<!--            <template slot-scope="scope">-->
<!--              <span>{{ parseTime(scope.row.createTime) }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            label="操作"-->
<!--            align="center"-->
<!--            width="160"-->
<!--            class-name="small-padding fixed-width"-->
<!--          >-->
<!--            <template slot-scope="scope" v-if="scope.row.userId !== 1">-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                type="text"-->
<!--                icon="el-icon-edit"-->
<!--                @click="handleUpdate(scope.row)"-->
<!--                v-hasPermi="['system:user:edit']"-->
<!--              >修改</el-button>-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                type="text"-->
<!--                icon="el-icon-delete"-->
<!--                @click="handleDelete(scope.row)"-->
<!--                v-hasPermi="['system:user:remove']"-->
<!--              >删除</el-button>-->
<!--              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">-->
<!--                <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>-->
<!--                <el-dropdown-menu slot="dropdown">-->
<!--                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key"-->
<!--                    v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>-->
<!--                  <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check"-->
<!--                    v-hasPermi="['system:user:edit']">分配角色</el-dropdown-item>-->
<!--                </el-dropdown-menu>-->
<!--              </el-dropdown>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="100%" append-to-body>

<!--      <el-form ref="form" :model="form" :rules="rules" label-width="80px">-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="用户昵称" prop="nickName">-->
<!--              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="归属部门" prop="deptId">-->
<!--              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择归属部门" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="手机号码" prop="phonenumber">-->
<!--              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="邮箱" prop="email">-->
<!--              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item v-if="form.userId == undefined" label="用户名称" prop="userName">-->
<!--              <el-input v-model="form.userName" placeholder="请输入用户名称" maxlength="30" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">-->
<!--              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password/>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="用户性别">-->
<!--              <el-select v-model="form.sex" placeholder="请选择性别">-->
<!--                <el-option-->
<!--                  v-for="dict in dict.type.sys_user_sex"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.label"-->
<!--                  :value="dict.value"-->
<!--                ></el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="状态">-->
<!--              <el-radio-group v-model="form.status">-->
<!--                <el-radio-->
<!--                  v-for="dict in dict.type.sys_normal_disable"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.value"-->
<!--                >{{dict.label}}</el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="岗位">-->
<!--              <el-select v-model="form.postIds" multiple placeholder="请选择岗位">-->
<!--                <el-option-->
<!--                  v-for="item in postOptions"-->
<!--                  :key="item.postId"-->
<!--                  :label="item.postName"-->
<!--                  :value="item.postId"-->
<!--                  :disabled="item.status == 1"-->
<!--                ></el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="角色">-->
<!--              <el-select v-model="form.roleIds" multiple placeholder="请选择角色">-->
<!--                <el-option-->
<!--                  v-for="item in roleOptions"-->
<!--                  :key="item.roleId"-->
<!--                  :label="item.roleName"-->
<!--                  :value="item.roleId"-->
<!--                  :disabled="item.status == 1"-->
<!--                ></el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="24">-->
<!--            <el-form-item label="备注">-->
<!--              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from "@/api/system/user";
import { getToken } from "@/utils/auth";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import * as echarts from 'echarts'

export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getDeptTree();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function() {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.deptId = undefined;
      this.$refs.tree.setCurrentKey(null);
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getUser().then(response => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "添加用户";
        this.form.password = this.initPassword;
      });

      this.initEcharts();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.$set(this.form, "postIds", response.postIds);
        this.$set(this.form, "roleIds", response.roleIds);
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
      }).then(({ value }) => {
          resetUserPwd(row.userId, value).then(response => {
            this.$modal.msgSuccess("修改成功，新密码是：" + value);
          });
        }).catch(() => {});
    },
    /** 分配角色操作 */
    handleAuthRole: function(row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    initEcharts () {
// 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(this.$refs.main);
      // 指定图表的配置项和数据
      var datas = [{"data":[[0,19],[5000,0],[10000,3],[15000,0],[20000,6],[25000,0],[30000,1],[35000,0],[40000,0],[45000,0],[50000,0],[55000,4],[60000,9],[65000,0],[70000,5],[75000,1],[80000,2],[85000,0],[90000,0],[95000,0],[100000,0],[105000,0],[110000,0],[115000,0],[120000,0],[125000,1],[130000,0],[135000,1],[140000,8],[145000,1],[150000,0],[155000,0],[160000,0],[165000,0],[170000,0],[175000,0],[180000,1],[185000,1],[190000,3],[195000,0]],"data2":[],"xMax":195000,"xMin":0},{"data":[[0,372],[5000,404],[10000,425],[15000,413],[20000,414],[25000,415],[30000,416],[35000,417],[40000,415],[45000,404],[50000,405],[55000,456],[60000,486],[65000,473],[70000,471],[75000,501],[80000,455],[85000,457],[90000,457],[95000,458],[100000,458],[105000,459],[110000,460],[115000,459],[120000,460],[125000,461],[130000,462],[135000,463],[140000,464],[145000,476],[150000,472],[155000,473],[160000,474],[165000,474],[170000,462],[175000,464],[180000,473],[185000,469],[190000,470],[195000,471]],"data2":[],"xMax":195000,"xMin":0},{"data":[[0,0],[5000,0],[10000,0],[15000,0],[20000,0],[25000,0],[30000,0],[35000,0],[40000,0],[45000,0],[50000,0],[55000,0],[60000,0],[65000,0],[70000,0],[75000,0],[80000,0],[85000,0],[90000,0],[95000,0],[100000,0],[105000,0],[110000,0],[115000,0],[120000,0],[125000,0],[130000,0],[135000,0],[140000,0],[145000,0],[150000,0],[155000,0],[160000,0],[165000,0],[170000,0],[175000,0],[180000,0],[185000,0],[190000,0],[195000,0]],"data2":[],"xMax":195000,"xMin":0},{"data":[[0,0],[5000,0],[10000,0],[15000,0],[20000,0],[25000,0],[30000,0],[35000,0],[40000,0],[45000,0],[50000,0],[55000,0],[60000,0],[65000,0],[70000,0],[75000,0],[80000,0],[85000,0],[90000,0],[95000,0],[100000,0],[105000,0],[110000,0],[115000,0],[120000,0],[125000,0],[130000,0],[135000,0],[140000,0],[145000,0],[150000,0],[155000,0],[160000,0],[165000,0],[170000,0],[175000,0],[180000,0],[185000,0],[190000,0],[195000,0]],"data2":[],"xMax":195000,"xMin":0},{"data":[[0,8.0],[5000,2.0],[10000,9.0],[15000,3.0],[20000,17.0],[25000,2.0],[30000,4.0],[35000,0.0],[40000,4.0],[45000,7.0],[50000,5.0],[55000,2.0],[60000,49.0],[65000,3.0],[70000,2.0],[75000,7.0],[80000,11.0],[85000,4.0],[90000,3.0],[95000,1.0],[100000,2.0],[105000,2.0],[110000,16.0],[115000,2.0],[120000,3.0],[125000,8.0],[130000,3.0],[135000,3.0],[140000,0.0],[145000,2.0],[150000,2.0],[155000,1.0],[160000,18.0],[165000,3.0],[170000,18.0],[175000,3.0],[180000,4.0],[185000,1.0],[190000,54.0],[195000,2.0]],"data2":[],"xMax":195000,"xMin":0},{"data":[[0,38.0],[5000,38.0],[10000,38.0],[15000,38.0],[20000,38.0],[25000,38.0],[30000,38.0],[35000,38.0],[40000,38.0],[45000,38.0],[50000,38.0],[55000,38.0],[60000,38.0],[65000,38.0],[70000,38.0],[75000,38.0],[80000,38.0],[85000,38.0],[90000,38.0],[95000,39.0],[100000,39.0],[105000,39.0],[110000,39.0],[115000,39.0],[120000,39.0],[125000,39.0],[130000,39.0],[135000,39.0],[140000,39.0],[145000,39.0],[150000,39.0],[155000,39.0],[160000,39.0],[165000,39.0],[170000,39.0],[175000,39.0],[180000,39.0],[185000,39.0],[190000,39.0],[195000,39.0]],"data2":[],"xMax":195000,"xMin":0},{"data":[[0,24],[5000,26],[10000,33],[15000,0],[20000,0],[25000,0],[30000,0],[35000,0],[40000,0],[45000,0],[50000,0],[55000,33],[60000,0],[65000,30],[70000,60],[75000,51],[80000,0],[85000,60],[90000,40],[95000,60],[100000,0],[105000,0],[110000,0],[115000,0],[120000,0],[125000,0],[130000,0],[135000,0],[140000,0],[145000,0],[150000,0],[155000,0],[160000,0],[165000,60],[170000,40],[175000,36],[180000,60],[185000,0],[190000,0],[195000,60]],"data2":[],"xMax":195000,"xMin":0}];
      var dataList = [];
      var valueList0 = [];
      var valueList1 = [];
      var valueList2 = [];
      var valueList3 = [];
      var valueList4 = [];
      var valueList5 = [];
      var valueList6 = [];
      datas.map(function(obj, index) {
        if(index === 0) {
          dataList = obj.data.map(function(item) {
            return Math.floor(item[0] / 1000);
          });
          valueList0 = obj.data.map(function(item) {
            return item[1];
          });
        }
        if(index === 1) {
          valueList1 = obj.data.map(function(item) {
            return item[1];
          });
        }
        if(index === 2) {
          valueList2 = obj.data.map(function(item) {
            return item[1];
          });
        }
        if(index === 3) {
          valueList3 = obj.data.map(function(item) {
            return item[1];
          });
        }
        if(index === 4) {
          valueList4 = obj.data.map(function(item) {
            return item[1];
          });
        }
        if(index === 5) {
          valueList5 = obj.data.map(function(item) {
            return item[1];
          });
        }
        if(index === 6) {
          valueList6 = obj.data.map(function(item) {
            return item[1];
          });
        }
      })
      var markLine = {
        data : [
          {type : 'average', name: '平均值'},//平均线
          { xAxis: '20' },//x='20'的垂直线   因为x轴默认类型为category,所以取值必须为字符串，若为数字则定位到第20个点
          { yAxis: 50 },//y=50的水平线
        ]
      };
      var option = {
        axisPointer: {
          link: [{
            xAxisIndex: 'all',
          }],
        },
        tooltip: {
          trigger: 'axis',
          formatter: function(params, b, c){
            let dataArr = [];
            let newDataArr = [];
            let formatterStr = ''
            params.map(function (item, index) {
              dataArr[item.seriesIndex] = item;
            })
            //console.log(dataArr);
            dataArr.map(function (item, index) {
              newDataArr.push({seriesName: item.seriesName, name: item.name, value: item.value})
            })
            console.log(newDataArr)
            newDataArr.map(function (item, index) {
              formatterStr += item.seriesName + ':(' + item.name + ',' + item.value + ')<br />'
            })
            return　formatterStr
          }
        },
        xAxis: [{
          type : 'category',
          //x轴类目名称列表
          data: dataList,
          //x 轴所在的 grid 的索引，默认0表示位于第一个 grid
          gridIndex: 0,
          name: '时间(s)',
          show: false
        }, {
          data: dataList,
          gridIndex: 1,
          name: '时间(s)'
        }, {
          data: dataList,
          gridIndex: 2,
          name: '时间(s)'
        }, {
          data: dataList,
          gridIndex: 3,
          name: '时间(s)'
        }, {
          data: dataList,
          gridIndex: 4,
          name: '时间(s)'
        }, {
          data: dataList,
          gridIndex: 5,
          name: '时间(s)'
        }, {
          data: dataList,
          gridIndex: 6,
          name: '时间(s)'
        }],
        yAxis: [{
          type : 'value',
          //展示网格线
          splitLine: {
            show: true
          },
          //y 轴所在的 grid 的索引，默认0表示位于第一个 grid
          gridIndex: 0,
          name: '占用(%)'
        }, {
          type : 'value',
          splitLine: {
            show: true
          },
          gridIndex: 1,
          name: '占用(MB)'
        }, {
          splitLine: {
            show: true
          },
          gridIndex: 2,
          name: '消耗(kb)'
        }, {
          splitLine: {
            show: true
          },
          gridIndex: 3,
          name: '消耗(kb)'
        }, {
          splitLine: {
            show: true
          },
          gridIndex: 4,
          name: '占用(%)'
        }, {
          splitLine: {
            show: true
          },
          gridIndex: 5,
          name: '温度(℃)'
        }, {
          splitLine: {
            show: true
          },
          gridIndex: 6,
          name: '均值(fps)'
        }],
        //配置grid组件在视图中位置, 每个占据整个canvas的10%
        grid: [{
          top: '4%',
          bottom: '86%',
        }, {
          top: '18%',
          bottom: '72%'
        }, {
          top: '32%',
          bottom: '58%'
        }, {
          top: '46%',
          bottom: '44%'
        }, {
          top: '60%',
          bottom: '30%'
        }, {
          top: '74%',
          bottom: '16%'
        }, {
          top: '88%',
          bottom: '2%'
        }],
        dataZoom: [
          //内置型缩放组件，即拖拽、滚轮缩放
          {
            type: 'inside',
            start: 0,
            end: 100,
            xAxisIndex: [0, 1, 2, 3, 4, 5, 6],//dataZoom 组件控制的x轴索引
          },
          //滑动型缩放组件，即底部缩放条缩放
          {
            type: 'slider',
            start: 0,
            end: 100,
            xAxisIndex: [0, 1, 2, 3, 4, 5, 6],
          },
        ],
        series: [{
          type: 'line',
          name: 'CPU占用',
          data: valueList0,//y轴类目数据
          xAxisIndex: 0,//使用x轴的索引
          yAxisIndex: 0,//使用y轴的索引
          //图表标注
          markPoint: markPoint('CPU占用'),
          //图表标线
          markLine : markLine
        }, {
          type: 'line',
          name: '内存占用',
          data: valueList1,
          xAxisIndex: 1,
          yAxisIndex: 1,
          markPoint: markPoint('内存占用'),
          markLine : markLine
        }, {
          type: 'line',
          name: '下行流量消耗',
          data: valueList2,
          xAxisIndex: 2,
          yAxisIndex: 2,
          markPoint: markPoint('下行流量消耗'),
          markLine : markLine
        }, {
          type: 'line',
          name: '上行流量消耗',
          data: valueList3,
          xAxisIndex: 3,
          yAxisIndex: 3,
          markPoint:  markPoint('上行流量消耗'),
          markLine : markLine
        }, {
          type: 'line',
          name: 'GPU占用',
          data: valueList4,
          xAxisIndex: 4,
          yAxisIndex: 4,
          markPoint:  markPoint('GPU占用'),
          markLine : markLine
        }, {
          type: 'line',
          name: '电池温度',
          data: valueList5,
          xAxisIndex: 5,
          yAxisIndex: 5,
          markPoint:  markPoint('电池温度'),
          markLine : markLine
        }, {
          type: 'line',
          data: valueList6,
          name: '帧率',
          xAxisIndex: 6,
          yAxisIndex: 6,
          markPoint:  markPoint('帧率'),
          markLine : markLine
        }]
      };

      myChart.setOption(option);

      function markPoint (seriesName) {
        return {
          itemStyle:{
            normal:{
              label:{
                show: true,
                color:"gray",
                formatter: function (param) {
                  console.log(param)
                  if (param.data.type == "max") {
                    return seriesName + "最大值" + param.value
                  }else if(param.data.type == "min"){
                    return seriesName + "最小值" + param.value
                  }
                }
              }
            }
          },
          data: [
            {type: 'max', name: '最大'},
            {type: 'min', name: '最小'}
          ]
        }
      }
    }

  }
};
</script>
