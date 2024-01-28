<script>

	import {onMount} from "svelte";
	import {isLogin} from "$lib/login_stores.js";
	import {logout} from "./member/login_check.js";
	import {toastNotice} from "./app.js";


	let loginNickname= $state("");

	async function logoutProcess() {

		await logout();
		toastNotice("로그아웃 성공");

		loginNickname = '';
	}

	onMount(() => {
        if (localStorage.getItem('nickname')) {
            loginNickname = localStorage.getItem('nickname') || '';
            isLogin.set(true);
        } else {
            isLogin.set(false);
        }
    });



</script>

<svelte:head>
	<title>Main</title>
	<meta name="description" content="Img_Forest" />
</svelte:head>

<section class="flex flex-row w-full">
	
</section>

