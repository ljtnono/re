<template>
    <div>
        <!-- 编辑用户控件 -->
        <modal
            title="编辑用户"
            width="600"
            v-model="showEditForm"
            class-name="vertical-center-modal">
            <!-- 控件 -->
            <user-edit-form />
        </modal>
        <!-- 新增用户控件 -->
        <modal
            title="新增用户"
            width="600"
            v-model="showAddForm"
            class-name="vertical-center-modal">
            <!-- 控件 -->
            <user-add-form />
        </modal>
        <Row style="padding:20px; margin-bottom: 40px">
            <!-- 用户总数 -->
            <Col span="7">
                <Card >

                </Card>
            </Col>
            <Col span="7" offset="1">
                <!--  -->
                <Card shadow>
                    <p slot="title">Use a card with a shadow effect</p>
                    <p>Content of card</p>
                    <p>Content of card</p>
                    <p>Content of card</p>
                </Card>
            </Col>
            <Col span="7" offset="2">
                <Card shadow>
                    <p slot="title">Use a card with a shadow effect</p>
                    <p>Content of card</p>
                    <p>Content of card</p>
                    <p>Content of card</p>
                </Card>
            </Col>
        </Row>
        <Card>
            <Form ref="searchForm" inline>
                <FormItem>
                    <Input type="text" v-model="getListParam.searchCondition" placeholder="用户名/手机/邮箱"/>
                </FormItem>
                <FormItem>
                    <Button type="primary" icon="ios-search" @click="search">搜索</Button>
                </FormItem>
                <FormItem>
                    <Button type="info" icon="md-add-circle" @click="add">新增用户</Button>
                </FormItem>
                <FormItem>
                    <Dropdown>
                        <Button type="primary">
                            更多操作
                            <Icon type="ios-arrow-down" />
                        </Button>
                        <DropdownMenu slot="list">
                            <DropdownItem>删除选中项</DropdownItem>
                            <DropdownItem>导出选中项</DropdownItem>
                        </DropdownMenu>
                    </Dropdown>
                </FormItem>
            </Form>
            <Table :columns="columns"
                   stripe
                   :data="userList"
                   :loading="loading"
                   @on-sort-change="sortChange"
                   ref="table">
                <template slot-scope="{ row, index }" slot="action">
                    <Button type="primary" size="small" style="margin-right: 5px" @click="edit(row)">编辑</Button>
                    <Button type="error" size="small" style="margin-right: 5px" @click="logicDelete(row)">删除</Button>
                </template>
            </Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page size="small" show-elevator show-sizer :total="total" :current="current" :page-size="pageSize">
                    </Page>
                </div>
            </div>
        </Card>
    </div>
</template>

<script>
import {getList, logicDelete} from "@/api/system/user";
import {UserAddForm, UserEditForm} from "@/components/system/user"

export default {
    name: "User",
    components: {
        UserAddForm,
        UserEditForm
    },
    computed: {

    },
    data() {
        return {
            columns: [
                {type: "selection", width: 60, align: "left"},
                {title: "id", width: 60, key: "id", align: "left"},
                {title: "用户名", key: "username", align: "left"},
                {
                    title: "角色", key: "roleName", align: "left", filters: [
                        {
                            label: "超级管理员",
                            value: 1
                        },
                        {
                            label: "测试",
                            value: 2
                        },
                        {
                            label: "游客",
                            value: 3
                        }
                    ],
                    filterRemote: this.roleFilterMethod, filterMultiple: false
                },
                {title: "电话", key: "phone", align: "left"},
                {title: "邮箱", key: "email", align: "left"},
                {title: "创建时间", key: "createTime", align: "left", sortable: "custom"},
                {title: "修改时间", key: "modifyTime", align: "left", sortable: "custom"},
                {title: "操作", width: 180, slot: "action", align: "left"}
            ],
            pageNum: 1,
            pageSize: 10,
            total: 1,
            current: 1,
            getListParam: {
                searchCondition: "",
                roleId: null,
                sortFieldList: [],
                sortTypeList: [],
                pageNum: 1,
                pageSize: 10
            },
            userList: [],
            loading: true,
            showEditForm: false,
            showAddForm: false
        }
    },
    methods: {
        // 搜索
        search() {
            this.getList().then(result => {
                this.setListData(result);
            }).catch(error => {
            });
        },
        // 调用获取用户列表接口返回其promise对象
        getList() {
            this.loading = true;
            return getList({...this.getListParam});
        },
        // 调用接口之后将接口返回的数据设置到data属性上
        setListData(result) {
            let data = result.data.data;
            this.userList = data.records;
            this.loading = false;
            // 当前页
            this.current = data.current;
            // 总条数
            this.total = data.total;
            // 每页条数
            this.pageSize = data.size;
        },
        // 每一列逻辑删除
        logicDelete(row) {
            // 然后重新请求一次
            let that = this;
            this.$Modal.confirm({
                title: "是否删除该用户",
                onOk() {
                    let idList = [row.id];
                    logicDelete({idList}).then(result => {
                        let data = result.data;
                        if (data.code === 0 && data.message === "success") {
                            this.$Message.success({
                                background: true,
                                content: "删除成功"
                            });
                            that.getList().then(result => {
                                that.setListData(result);
                            }).catch(error => {
                            });
                        }
                    }).catch(error => {
                    });
                },
                onCancel() {
                    this.$Message.info({
                        background: true,
                        content: "已取消删除"
                    });
                }
            });
        },
        // 角色筛选条件
        roleFilterMethod(row) {
            this.getListParam.roleId = row[0];
            this.getList().then(result => {
                this.setListData(result);
            }).catch(error => {
            });
            return true;
        },
        // 编辑用户信息
        edit(row) {
            this.showEditForm = !this.showEditForm;
        },
        // 新增用户
        add(row) {
            this.showAddForm = !this.showAddForm;
        },
        // 排序条件变化
        sortChange(row) {
            // 首先清除参数
            this.clearSortParam();
            let key = row.key;
            let order = row.order;
            if (key === "createTime") {
                this.getListParam.sortFieldList.push("create_time");
                if (order === "desc") {
                    this.getListParam.sortTypeList.push(2);
                } else {
                    this.getListParam.sortTypeList.push(1);
                }
            } else if (key === "modifyTime") {
                this.getListParam.sortFieldList.push("modify_time");
                if (order === "desc") {
                    this.getListParam.sortTypeList.push(2);
                } else {
                    this.getListParam.sortTypeList.push(1);
                }
            }
            // 重新请求一次
            this.getList().then(result => {
                this.setListData(result);
            }).catch(error => {
            });
        },
        // 清除获取列表的参数
        clearGetListParam() {
            this.getListParam = {
                username: '',
                roleId: null,
                sortFieldList: [],
                sortTypeList: [],
                pageNum: 1,
                pageSize: 10
            }
        },
        // 清除排序参数
        clearSortParam() {
            this.getListParam.sortTypeList = [];
            this.getListParam.sortFieldList = [];
        }
    },
    mounted() {
        // 先清除数据
        this.clearGetListParam();
        // 获取并更新表格数据
        this.getList().then(result => {
            this.setListData(result);
        }).catch(error => {
        });
    }
}
</script>

<style scoped lang="less">
    .vertical-center-modal {
        display: flex;
        align-items: center;
        justify-content: center;
        .ivu-modal{
            top: 0;
        }
    }
</style>
