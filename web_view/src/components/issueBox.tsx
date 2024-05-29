import { Issue } from "@/lib/types";
import Link from "next/link";

export function IssueBox({ data }: { data: Issue }) {
	return (
		<div
			className="rounded-lg border shadow-sm w-[calc(100%-4rem)] my-4"
			data-v0-t="card"
		>
			<Link href={"/issues/" + data.id}>
				<div className="flex flex-col space-y-1.5 p-6">
					<h3 className="whitespace-nowrap text-2xl font-semibold leading-none tracking-tight">
						{data.title}
					</h3>
					<p className="text-sm text-muted-foreground pt-2 overflow-hidden whitespace-nowrap text-ellipsis break-all">
						{data.content}
					</p>
				</div>
				<div className="p-6 grid gap-4">
					<div className="grid grid-cols-2 gap-4">
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Reporter
							</p>
							<p>{data.reporter}</p>
						</div>
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Assignee
							</p>
							<p>{data.assignee.join(", ")}</p>
						</div>
					</div>
					<div className="grid grid-cols-2 gap-4">
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Fixer
							</p>
							<div>{data.fixer}</div>
						</div>
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Status
							</p>
							<div>{data.status}</div>
						</div>
					</div>
					<div className="grid grid-cols-2 gap-4">
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Priority
							</p>
							<div>{data.priority}</div>
						</div>
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Reported
							</p>
							<p>{new Date(data.createDate).toLocaleString()}</p>
						</div>
					</div>
				</div>
			</Link>
		</div>
	);
}
