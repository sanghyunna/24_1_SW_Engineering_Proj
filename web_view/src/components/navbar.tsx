"use client";

import { useAccountContext } from "@/app/layout";
import { useContext } from "react";

export function Navbar() {
	const { name, token, setAccount } = useAccountContext();

	const tempClick = () => {
		if (setAccount) {
			setAccount("testName", "testToken");
		}
	};

	let account;
	if (name === "") {
		account = (
			<a
				className="transition-colors ease-out my-1 text-sm text-gray-700 dark:text-gray-200 hover:text-blue-500 dark:hover:text-blue-400 md:mx-4 md:my-0"
				onClick={tempClick}
			>
				로그인
			</a>
		);
	} else {
		account = (
			<a className="transition-colors ease-out my-1 text-sm text-gray-700 dark:text-gray-200 hover:text-blue-500 dark:hover:text-blue-400 md:mx-4 md:my-0">
				로그아웃
			</a>
		);
	}

	return (
		<nav className="bg-white dark:bg-gray-800 shadow">
			<div className="container mx-auto px-6 py-3 md:flex md:justify-between md:items-center">
				<div className="flex justify-between items-center">
					<div>
						<a
							className="transition-colors ease-out text-gray-800 dark:text-white text-xl font-bold md:text-2xl hover:text-gray-700 dark:hover:text-gray-300"
							href="/"
						>
							Issue Tracker
						</a>
					</div>

					<div className="flex flex-col md:flex-row md:mx-6 pl-16">
						<a
							className="transition-colors ease-out my-1 text-sm text-gray-700 dark:text-gray-200 hover:text-blue-500 dark:hover:text-blue-400 md:mx-4 md:my-0"
							href="/"
						>
							대시보드
						</a>
						<a
							className="transition-colors ease-out my-1 text-sm text-gray-700 dark:text-gray-200 hover:text-blue-500 dark:hover:text-blue-400 md:mx-4 md:my-0"
							href="#"
						>
							새 이슈 추가
						</a>
						<a
							className="transition-colors ease-out my-1 text-sm text-gray-700 dark:text-gray-200 hover:text-blue-500 dark:hover:text-blue-400 md:mx-4 md:my-0"
							href="#"
						>
							이슈 검색
						</a>
						<a
							className="transition-colors ease-out my-1 text-sm text-gray-700 dark:text-gray-200 hover:text-blue-500 dark:hover:text-blue-400 md:mx-4 md:my-0"
							href="#"
						>
							계정 추가
						</a>
					</div>
				</div>

				<div className="md:flex items-center">
					<div className="flex justify-center md:block">
						{/* <a
							className="relative text-gray-700 dark:text-gray-200 hover:text-gray-600 dark:hover:text-gray-300"
							href="https://github.com/sanghyunna/24_1_SW_Engineering_Proj"
						>
							Github
						</a> */}
						{account}
					</div>
				</div>
			</div>
		</nav>
	);
}
