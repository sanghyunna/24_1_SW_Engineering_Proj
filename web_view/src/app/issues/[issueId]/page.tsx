"use client";

import { CommentBox } from "@/components/commentBox";
import { CreateCommentModal } from "@/components/modal/createCommentModal";
import { baseURL } from "@/lib/constants";
import { Comment, Issue } from "@/lib/types";
import { useEffect, useState } from "react";

export default function ({ params }: { params: { issueId: string } }) {
	const [issue, setIssue] = useState<Issue>({} as Issue);
	const [comments, setComments] = useState<Comment[]>([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);

	useEffect(() => {
		setLoading(true);
		const fetchData = async () => {
			try {
				const issueRes = await fetch(`${baseURL}/issue/${params.issueId}`).then(
					(res) => res.json()
				);
				setIssue(issueRes);

				const commentsRes = await fetch(
					`${baseURL}/issue/${params.issueId}/comment`
				).then((res) => res.json());
				setComments(commentsRes);
			} catch (err) {
				setError("Failed to fetch data");
			} finally {
				setLoading(false);
			}
		};
		fetchData();
	}, []);

	if (loading) return <div>Loading...</div>;
	if (error) return <div>{error}</div>;

	return (
		<div>
			<div className="mb-12">
				<div className="text-5xl font-bold">{issue.title}</div>
			</div>
			<div className="px-6 py-5 grid grid-cols-2 gap-4 rounded-lg border">
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Reported</p>
					<p className="text-gray-900 font-medium">
						{new Date(issue.createDate).toLocaleString()}
					</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">
						Last Updated
					</p>
					<p className="text-gray-900 font-medium">
						{new Date(issue.updateDate).toLocaleString()}
					</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Reporter</p>
					<p className="text-gray-900 font-medium">{issue.reporter}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Assignee</p>
					<p className="text-gray-900 font-medium">
						{issue.assignee.join(", ")}
					</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Fixer</p>
					<p className="text-gray-900 font-medium">{issue.fixer}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Due Date</p>
					<p className="text-gray-900 font-medium">
						{new Date(issue.dueDate).toLocaleString()}
					</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Status</p>
					<p className="text-gray-900 font-medium">{issue.status}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Priority</p>
					<p className="text-gray-900 font-medium">{issue.priority}</p>
				</div>
			</div>

			<div className="px-2 py-4 mt-16 border-b border-gray-400">
				<h3 className="text-xl font-bold">Content</h3>
			</div>
			<div className="px-2 my-6 break-all">{issue.content}</div>

			<div className="px-2 py-4 mt-16 border-b border-gray-400">
				<h3 className="text-xl font-bold">Comment</h3>
			</div>
			<div className="flex flex-col place-items-center pt-6">
				<CreateCommentModal issueId={issue.id} />
				{comments.map((comment) => (
					<CommentBox key={comment.id} data={comment} />
				))}
			</div>
		</div>
	);
}
