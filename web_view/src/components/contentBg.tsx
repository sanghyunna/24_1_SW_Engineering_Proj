export function ContentBg({ children }: { children: React.ReactNode }) {
	return (
		<div className="w-3/4 max-w-fine p-16 mx-2 my-12 overflow-auto">
			{children}
		</div>
	);
}
