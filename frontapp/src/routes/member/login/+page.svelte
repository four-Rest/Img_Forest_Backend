<script>
	import { goto } from '$app/navigation';
	import { toastNotice } from '../../app.js';

	$: signupData = {
		username: '',
		password: ''
	};

	const apiBaseUrl = import.meta.env.VITE_CORE_API_BASE_URL;

	async function login() {
		await fetch(`${apiBaseUrl}/member/login`, {
			headers: {
				'Content-Type': 'application/json'
			},
			method: 'POST',
			body: JSON.stringify(signupData)
		});
		toastNotice('로그인 완료.');
		await goto('/');
	}
</script>

<svelte:head>
	<title>Home</title>
	<meta name="description" content="Svelte demo app" />
</svelte:head>

<section class="">
	<div class="card shadow-xl">
		<div class="card-body p-1">
			<h1 class="card-title justify-center">로그인</h1>
			<form class="p-5">
				<div class="card-body p-1">
					<label class="card-title" for="username">사용자ID</label>
					<input
						type="text"
						class="textarea textarea-bordered card-actions justify-end"
						placeholder="username"
						bind:value={signupData.username}
					/>
				</div>
				<div class="card-body p-1">
					<label class="card-title" for="password1">비밀번호</label>
					<input
						type="password"
						class="textarea textarea-bordered"
						placeholder="password"
						bind:value={signupData.password}
					/>
				</div>
				<button class="btn" type="submit" on:click={(event) => login()}>로그인</button>
			</form>
		</div>
	</div>
</section>
