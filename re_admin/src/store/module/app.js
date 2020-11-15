import {
    getBreadCrumbList,
    setTagNavListInLocalstorage,
    getMenuByRouter,
    getTagNavListFromLocalstorage,
    getHomeRoute,
    getNextRoute,
    routeHasExist,
    routeEqual,
    getRouteTitleHandled,
    localSave,
    localRead
} from "@/libs/util";
import router from "@/router";
import routers from "@/router/routers";
import * as systemConstant from "@/constant/system/constant";

const {HOME_PAGE_NAME} = systemConstant;

const closePage = (state, route) => {
    const nextRoute = getNextRoute(state.tagNavList, route);
    state.tagNavList = state.tagNavList.filter(item => {
        return !routeEqual(item, route);
    });
    router.push(nextRoute);
};

export default {
    state: {
        breadCrumbList: [],
        tagNavList: [],
        homeRoute: {},
        local: localRead("local"),
        errorList: [],
        hasReadErrorPage: false
    },
    getters: {
        menuList: (state, getters, rootState) => getMenuByRouter(routers, rootState.user.access),
        errorCount: state => state.errorList.length
    },
    mutations: {
        setBreadCrumb(state, route) {
            state.breadCrumbList = getBreadCrumbList(route, state.homeRoute);
        },
        setHomeRoute(state, routes) {
            state.homeRoute = getHomeRoute(routes, HOME_PAGE_NAME);
        },
        setTagNavList(state, list) {
            let tagList = [];
            if (list) {
                tagList = [...list];
            } else tagList = getTagNavListFromLocalstorage() || [];
            if (tagList[0] && tagList[0].name !== HOME_PAGE_NAME) tagList.shift();
            let homeTagIndex = tagList.findIndex(item => item.name === HOME_PAGE_NAME);
            if (homeTagIndex > 0) {
                let homeTag = tagList.splice(homeTagIndex, 1)[0];
                tagList.unshift(homeTag);
            }
            state.tagNavList = tagList;
            setTagNavListInLocalstorage([...tagList]);
        },
        closeTag(state, route) {
            let tag = state.tagNavList.filter(item => routeEqual(item, route));
            route = tag[0] ? tag[0] : null;
            if (!route) return;
            closePage(state, route);
        },
        addTag(state, {route, type = "unshift"}) {
            let router = getRouteTitleHandled(route);
            if (!routeHasExist(state.tagNavList, router)) {
                if (type === "push") state.tagNavList.push(router);
                else {
                    if (router.name === HOME_PAGE_NAME) state.tagNavList.unshift(router);
                    else state.tagNavList.splice(1, 0, router);
                }
                setTagNavListInLocalstorage([...state.tagNavList]);
            }
        },
        setLocal(state, lang) {
            localSave("local", lang);
            state.local = lang;
        }
    },
    actions: {}
};
