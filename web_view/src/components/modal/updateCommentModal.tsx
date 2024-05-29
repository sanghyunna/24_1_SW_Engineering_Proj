"use client";

import { useAccountContext } from "@/app/layout";
import { baseURL } from "@/lib/constants";
import { Comment } from "@/lib/types";
import React, { useState } from "react";

export function UpdateCommentModal({ comment }: { comment: Comment }) {
	const [isOpen, setIsOpen] = useState(false);
	const [bodyData, setBodyData] = useState({
		content: "",
	});
	const { name, token, setAccount } = useAccountContext();

	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setBodyData((prevState) => ({
			...prevState,
			[name]: value,
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
		try {
			const response = await fetch(`${baseURL}/comment/${comment.id}`, {
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
			alert("코멘트 수정 완료");
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
					className="inline-flex items-center justify-center whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-primary/90 h-8 px-4 py-2  bg-blue-500 text-white rounded-lg"
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
								<h2 className="text-2xl font-bold">Edit Comment</h2>
								<p className="text-gray-500">코멘트를 수정합니다.</p>
							</div>
							<div className="space-y-2 mb-4">
								<label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
									Content
								</label>
								<input
									className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
									name="content"
									defaultValue={comment.content}
									onChange={handleInputChange}
								/>
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
