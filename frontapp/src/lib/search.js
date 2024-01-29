// @ts-nocheck
import { goto } from '$app/navigation';
import { isLogin } from '$lib/login_stores.js';
import { onMount } from 'svelte';

const apiBaseUrl = import.meta.env.VITE_CORE_API_BASE_URL;

/**
 * @param {string} value
 */

let searchData = { searchValue: '' };

export async function searchValue() {
	await fetch(`${apiBaseUrl}/article/{value}}`, {
		// 검색 api 만든 후 수정
		headers: {
			'Content-Type': 'application/json'
		},
		method: 'POST',
		credentials: 'include',
		body: JSON.stringify(searchData)
	}).then((res) => res.json());

	goto(`${apiBaseUrl}/article/list/{res.value}`); // 추후 value 기반한 게시물 페이지를 goto로 연결
}

export function viewSearchBox() {
	var input = document.getElementById('search-input');
	console.log('클릭되나?');
	input.style.display = 'block'; // input을 보이게 설정
}
