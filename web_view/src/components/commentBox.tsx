import { Comment } from "@/lib/types";

export function CommentBox({ data }: { data: Comment }) {
	return (
		<div className="rounded-lg border bg-card text-card-foreground p-4 my-4 shadow-md w-full">
			<div className="flex items-start gap-4">
				<span className="relative flex h-10 w-10 overflow-hidden rounded-full shrink-0">
					<img
						className="aspect-square h-full w-full"
						src="/placeholder-user.jpg"
					/>
				</span>
				<div className="flex-1">
					<div className="flex items-center justify-between">
						<div className="font-medium">{data.commentOwner}</div>
						<div className="text-sm text-gray-500 dark:text-gray-400">
							{new Date(data.createDate).toLocaleString()}
							<span>에 작성함</span>
						</div>
					</div>
					<p className="mt-2 text-sm leading-relaxed break-all">
						{data.content}
					</p>
					<div className="mt-2 text-sm text-gray-500 dark:text-gray-400">
						{new Date(data.updateDate).toLocaleString()}
						<span>에 수정함</span>
					</div>
				</div>
			</div>
		</div>
	);
}
