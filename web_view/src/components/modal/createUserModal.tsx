"use client";

import { baseURL } from "@/lib/constants";
import React, { useState } from "react";

export function CreateUserModal() {
	const [isOpen, setIsOpen] = useState(false);
	const [bodyData, setBodyData] = useState({ name: "", password: "" });

	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setBodyData((prevState) => ({
			...prevState,
			[name]: value,
		}));
	};

	const handleSubmit = async () => {
		try {
			const response = await fetch(`${baseURL}/user`, {
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
			alert("계정 생성 완료");
		} catch (error) {
			console.error(error);
			alert("오류가 발생했습니다.");
		}
		setIsOpen(false);
	};

	return (
		<>
			<button
				className="transition-colors ease-out my-1 text-sm text-gray-700  hover:text-blue-500  md:mx-4 md:my-0"
				onClick={() => setIsOpen(true)}
			>
				새 계정 생성
			</button>
			{isOpen && (
				<div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
					<div className="w-1/2 max-w-[50rem] rounded-lg bg-white p-6 shadow-lg">
						<div className="space-y-4">
							<div className="text-center">
								<h2 className="text-2xl font-bold">Sign in</h2>
								<p className="text-gray-500">
									이름과 비밀번호를 입력하여 계정을 생성하세요.
								</p>
							</div>
							<div className="space-y-2 mb-4">
								<label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
									Name*
								</label>
								<input
									className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
									name="name"
									onChange={handleInputChange}
								/>
							</div>
							<div className="space-y-2 mb-4">
								<label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
									Password*
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
								Sign in
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
