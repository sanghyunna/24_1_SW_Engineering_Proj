import { IssueStats } from "@/lib/types";

export function StatsBox({ data }: { data: IssueStats }) {
	return (
		<div className="rounded-lg border text-card-foreground shadow-sm w-full mt-20 mb-8">
			<div className="flex flex-col space-y-1.5 p-6">
				<h3 className="whitespace-nowrap text-2xl font-semibold leading-none tracking-tight">
					Issue Statistics
				</h3>
			</div>
			<div className="p-6 grid gap-4">
				<div className="grid grid-cols-2 gap-4">
					<div className="bg-gray-100 p-4 rounded-lg">
						<div className="text-2xl font-bold">{data.todayCount}</div>
						<p className="text">Today's Issues</p>
					</div>
					<div className="bg-gray-100 p-4 rounded-lg">
						<div className="text-2xl font-bold">{data.monthlyCount}</div>
						<p className="text">This Month</p>
					</div>
				</div>
				<div className="grid gap-4">
					<div>
						<h3 className="text-lg font-medium mb-2">Status</h3>
						<div className="grid grid-cols-5 gap-2">
							<div className="bg-green-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-green-600  ">
									NEW
								</div>
								<p className="text-lg text-gray-500 ">{data.statusCount.NEW}</p>
							</div>
							<div className="bg-amber-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-amber-600 ">
									ASSIGNED
								</div>
								<p className="text-lg text-gray-500 ">
									{data.statusCount.ASSIGNED}
								</p>
							</div>
							<div className="bg-sky-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-sky-600 ">FIXED</div>
								<p className="text-lg text-gray-500 ">
									{data.statusCount.FIXED}
								</p>
							</div>
							<div className="bg-slate-200 p-2 rounded-lg ">
								<div className="text-base font-medium text-slate-800 ">
									CLOSED
								</div>
								<p className="text-lg text-gray-500 ">
									{data.statusCount.CLOSED}
								</p>
							</div>
							<div className="bg-purple-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-purple-600 ">
									REOPENED
								</div>
								<p className="text-lg text-gray-500 ">
									{data.statusCount.REOPENED}
								</p>
							</div>
						</div>
					</div>
					<div>
						<h3 className="text-lg font-medium mb-2">Priority</h3>
						<div className="grid grid-cols-5 gap-2">
							<div className="bg-gray-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-gray-600  ">
									TRIVIAL
								</div>
								<p className="text-lg text-gray-500 ">
									{data.priorityCount.TRIVIAL}
								</p>
							</div>
							<div className="bg-green-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-green-600 ">
									MINOR
								</div>
								<p className="text-lg text-gray-500 ">
									{data.priorityCount.MINOR}
								</p>
							</div>
							<div className="bg-yellow-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-yellow-600 ">
									MAJOR
								</div>
								<p className="text-lg text-gray-500 ">
									{data.priorityCount.MAJOR}
								</p>
							</div>
							<div className="bg-orange-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-orange-600 ">
									CRITICAL
								</div>
								<p className="text-lg text-gray-500 ">
									{data.priorityCount.CRITICAL}
								</p>
							</div>
							<div className="bg-red-100 p-2 rounded-lg ">
								<div className="text-base font-medium text-red-600 ">
									BLOCKER
								</div>
								<p className="text-lg text-gray-500 ">
									{data.priorityCount.BLOCKER}
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
