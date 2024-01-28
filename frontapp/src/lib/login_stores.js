// 전역상태관리에 사용되는 스벨트의 store

import {writable} from "svelte/store";

export let isLogin= writable(false);  // writable-> 값이 변경 가능한 store 생성