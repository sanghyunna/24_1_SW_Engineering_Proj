"use client";

import { baseURL } from "@/lib/constants";
import { Issue } from "@/lib/types";
import { Dispatch, SetStateAction, useState } from "react";

export function SearchBox({
	projectId,
	setData,
}: {
	projectId: number;
	setData: Dispatch<SetStateAction<Issue[]>>;
}) {
	const [keyword, setKeyword] = useState<string>("");

	const handleSearch = async () => {
		try {
			let response;
			if (keyword === "") {
				response = await fetch(`${baseURL}/project/${projectId}/issue`);
			} else {
				response = await fetch(
					`${baseURL}/project/${projectId}/issue/search?keyword=${encodeURIComponent(
						keyword
					)}`
				);
			}

			if (!response.ok) throw new Error("Failed to fetch search results");
			const result: Issue[] = await response.json();
			setData(result);
		} catch (error) {
			console.error(error);
			setData([]);
		}
	};

	const enterkey = (e: React.KeyboardEvent<HTMLInputElement>) => {
		if (e.key === "Enter") {
			handleSearch();
		}
	};

	return (
		<div className="w-full max-w-fine mt-28">
			<div className="flex items-center rounded-2xl bg-white shadow px-4 py-2 border-2 border-slate-300 h-14">
				<input
					className="w-full bg-transparent focus:outline-none"
					placeholder="무엇이든 검색하세요. (ex: Title, Assignee, Status ... )"
					type="text"
					value={keyword}
					onChange={(e) => setKeyword(e.target.value)}
					onKeyDown={enterkey}
				/>
				<button
					className="ml-2 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl px-5 h-9"
					onClick={handleSearch}
				>
					Search
				</button>
			</div>
		</div>
	);
}
