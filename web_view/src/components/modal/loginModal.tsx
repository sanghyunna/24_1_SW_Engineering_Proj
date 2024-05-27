"use client";

import { useAccountContext } from "@/app/layout";
import { baseURL } from "@/lib/constants";
import React, { useState } from "react";

export function LoginModal() {
	const [isOpen, setIsOpen] = useState(false);
	const [bodyData, setBodyData] = useState({ name: "", password: "" });
	const { name, token, setAccount } = useAccountContext();

	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setBodyData((prevState) => ({
			...prevState,
			[name]: value,
		}));
	};

	const handleSubmit = async () => {
		try {
			const response = await fetch(`${baseURL}/login`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(bodyData),
			});
			if (!response.ok) {
				throw new Error(`HTTP error ${response.status}`);
			}
			const data = await response.json();
			console.log(data);
			setAccount(data.name, data.token);
		} catch (error) {
			console.error(error);
			alert("이름 또는 비밀번호가 다릅니다.");
		}
		setIsOpen(false);
	};

	const logout = async () => {
		console.log(token);
		try {
			const response = await fetch(`${baseURL}/logout`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ token: token }),
			});
			if (!response.ok) {
				throw new Error(`HTTP error ${response.status}`);
			}
			setAccount("", "");
			alert("로그아웃 되었습니다.");
		} catch (error) {
			console.error(error);
			setAccount("", "");
			alert("오류가 발생했습니다.");
		}
	};

	return (
		<>
			{name === "" && (
				<button
					className="transition-colors ease-out my-1 text-sm text-gray-700  hover:text-blue-500  md:mx-4 md:my-0"
					onClick={() => setIsOpen(true)}
				>
					로그인
				</button>
			)}
			{name !== "" && (
				<button
					className="transition-colors ease-out my-1 text-sm text-gray-700  hover:text-blue-500  md:mx-4 md:my-0"
					onClick={logout}
				>
					로그아웃
				</button>
			)}
			{isOpen && (
				<div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
					<div className="w-1/2 max-w-[50rem] rounded-lg bg-white p-6 shadow-lg">
						<div className="space-y-4">
							<div className="text-center">
								<h2 className="text-2xl font-bold">Login</h2>
								<p className="text-gray-500">
									이름과 비밀번호를 입력하여 로그인하세요.
								</p>
							</div>
							<div className="space-y-2 mb-4">
								<label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
									Name
								</label>
								<input
									className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
									name="name"
									onChange={handleInputChange}
								/>
							</div>
							<div className="space-y-2 mb-4">
								<label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
									Password
								</label>
								<input
									className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
									type="password"
									name="password"
									onChange={handleInputChange}
								/>
							</div>
							<button
								className="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 w-full bg-black text-white"
								type="submit"
								onClick={handleSubmit}
							>
								Login
							</button>
							<button
								className="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 w-full"
								type="button"
								onClick={() => setIsOpen(false)}
							>
								Close
							</button>
						</div>
					</div>
				</div>
			)}
		</>
	);
}
