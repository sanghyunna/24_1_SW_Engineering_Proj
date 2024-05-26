export function SearchBox() {
	return (
		<div className="w-[calc(100%-6rem)] max-w-fine mt-32">
			<div className="flex items-center rounded-2xl bg-white shadow px-4 py-2 border-2 border-slate-300 h-14">
				<input
					className="w-full bg-transparent focus:outline-none"
					placeholder="무엇이든 검색하세요. (ex: Assignee, issue, reporter)"
					type="text"
				/>
				<button className="ml-2 bg-blue-500 hover:bg-blue-600 text-white rounded-2xl px-5 h-9">
					Search
				</button>
			</div>
		</div>
	);
}
