"use client";

import { useAccountContext } from "@/app/layout";
import { baseURL } from "@/lib/constants";
import { Issue } from "@/lib/types";
import React, { useState } from "react";

export function UpdateIssueStatusModal({ issue }: { issue: Issue }) {
	const [isOpen, setIsOpen] = useState(false);
	const [bodyData, setBodyData] = useState({
		statusName: issue.status,
	});
	const { name, token, setAccount } = useAccountContext();

	const clickButton = (value: string) => {
		setBodyData((prevState) => ({
			...prevState,
			statusName: value,
		}));
	};

	const handleSubmit = async () => {
		const filteredData = Object.fromEntries(
			Object.entries(bodyData).filter(([key, value]) => {
				if (Array.isArray(value)) {
					return value.length > 0;
				}
				return value !== "";
			})
		);

		const tokenData = { token: token };
		const combinedData = { ...filteredData, ...tokenData };

		console.log(combinedData);
		try {
			const response = await fetch(`${baseURL}/issue/${issue.id}/status`, {
				method: "PATCH",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(combinedData),
			});
			if (!response.ok) {
				throw new Error(`HTTP error ${response.status}`);
			}
			const data = await response.json();
			console.log(data);
			alert("이슈 상태 수정 완료");
			location.reload();
		} catch (error) {
			console.error(error);
			alert("오류가 발생했습니다.");
		}
		setIsOpen(false);
	};

	return (
		<>
			{name !== "" && (
				<button
					className="inline-flex items-center justify-center whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-primary/90 h-8 px-2 py-2  bg-blue-500 text-white rounded-md mr-8"
					onClick={() => setIsOpen(true)}
				>
					수정
				</button>
			)}

			{isOpen && (
				<div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
					<div className="w-1/2 max-w-[50rem] rounded-lg bg-white p-6 shadow-lg">
						<div className="space-y-4">
							<div className="text-center">
								<h2 className="text-2xl font-bold">Edit Issue Status</h2>
								<p className="text-gray-500">이슈 Status를 수정합니다.</p>
							</div>
							<div className="flex justify-between py-8">
								<button
									className="px-4 py-2 bg-green-200 rounded-xl"
									onClick={() => {
										clickButton("NEW");
									}}
								>
									NEW
								</button>
								<button
									className="px-4 py-2 bg-amber-200 rounded-xl"
									onClick={() => {
										clickButton("ASSIGNED");
									}}
								>
									ASSIGNED
								</button>
								<button
									className="px-4 py-2 bg-sky-200 rounded-xl"
									onClick={() => {
										clickButton("FIXED");
									}}
								>
									FIXED
								</button>
								<button
									className="px-4 py-2 bg-slate-200 rounded-xl"
									onClick={() => {
										clickButton("CLOSED");
									}}
								>
									CLOSED
								</button>
								<button
									className="px-4 py-2 bg-purple-200 rounded-xl"
									onClick={() => {
										clickButton("REOPENED");
									}}
								>
									REOPENED
								</button>
							</div>
							<div className="flex justify-center">
								<div>변경 :</div>
								<div className="pl-2">{bodyData.statusName}</div>
							</div>
							<button
								className="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 w-full bg-black text-white"
								type="submit"
								onClick={handleSubmit}
							>
								Edit
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
