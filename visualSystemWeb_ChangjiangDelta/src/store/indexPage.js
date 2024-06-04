import { defineStore } from 'pinia';
export const indexPageStore = defineStore('sidebar', {
    state: () => {
        return {
            //地图信息
            myMapInfo: {
                zoom: 1.2,
                // 地图点上弹出框的信息
                orgFxInfo: [],
                // 地图上的点
                mapPointsData: [],
            },
            orgInfo:{},
            orgList: [],
            selectDate: ''
        }
    },
    getters: {
		getOrgInfo(){
			if(window.sessionStorage.getItem('orgInfo') && window.sessionStorage.getItem('orgInfo') != 'undefined') {
				this.orgInfo = JSON.parse(window.sessionStorage.getItem('orgInfo'));
			}
    		return this.orgInfo;
    	},
    	getOrgList(){
			if(window.sessionStorage.getItem('orgList') && window.sessionStorage.getItem('orgList') != 'undefined') {
				this.orgInfo = JSON.parse(window.sessionStorage.getItem('orgList'));
			}
    		return this.orgList;
    	},
    	getSelectDate(){
			if(window.sessionStorage.getItem('selectDate') && window.sessionStorage.getItem('selectDate') != 'undefined') {
				this.selectDate = window.sessionStorage.getItem('selectDate');
			}
    		return this.selectDate;
    	},
    },
    actions: {
    	setOrgInfo(obj){
    		this.orgInfo = obj;
    		sessionStorage.setItem('orgInfo', JSON.stringify(obj));
    	},
    	setOrgList(list){
    		this.orgList = list;
    		sessionStorage.setItem('orgList', JSON.stringify(list));
    	},
    	setSelectDate(val){
    		this.selectDate = val;
    		sessionStorage.setItem('selectDate', val);
    	}
    }
})