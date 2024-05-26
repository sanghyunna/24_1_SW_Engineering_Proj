import Link from "next/link";

export function IssueBox() {
	return (
		<div
			className="rounded-lg border shadow-sm w-[calc(100%-4rem)] my-4"
			data-v0-t="card"
		>
			<Link href={"/projects/1/issues/1"}>
				<div className="flex flex-col space-y-1.5 p-6">
					<h3 className="whitespace-nowrap text-2xl font-semibold leading-none tracking-tight">
						Issue #123: Server Outage
					</h3>
					<p className="text-sm text-muted-foreground">
						The website is experiencing a server outage, causing users to be
						unable to access the platform.
					</p>
				</div>
				<div className="p-6 grid gap-4">
					<div className="grid grid-cols-2 gap-4">
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Reporter
							</p>
							<p>John Doe</p>
						</div>
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Status
							</p>
							<div>In Progress</div>
						</div>
					</div>
					<div className="grid grid-cols-2 gap-4">
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Priority
							</p>
							<div>High</div>
						</div>
						<div className="space-y-1">
							<p className="text-sm font-medium text-gray-500 dark:text-gray-400">
								Created
							</p>
							<p>2023-05-24</p>
						</div>
					</div>
				</div>
			</Link>
		</div>
	);
}
