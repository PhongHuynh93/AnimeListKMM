//
//  ListSeparatorStyle.swift
//  iosApp
//
//  Created by Coder on 10/31/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI

// not work on ios 14
struct ListSeparatorStyle: ViewModifier {
    
    let style: UITableViewCell.SeparatorStyle
    
    func body(content: Content) -> some View {
        content
            .onAppear() {
                UITableView.appearance().separatorStyle = self.style
            }
    }
}

extension View {
    
    func listSeparatorStyle(style: UITableViewCell.SeparatorStyle) -> some View {
        ModifiedContent(content: self, modifier: ListSeparatorStyle(style: style))
    }
}
